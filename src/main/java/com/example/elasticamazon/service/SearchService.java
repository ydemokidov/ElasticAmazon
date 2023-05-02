package com.example.elasticamazon.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.example.elasticamazon.model.Book;
import com.example.elasticamazon.model.dto.AvgReviewsByPublisher;
import com.example.elasticamazon.model.dto.BooksByPublisher;
import com.example.elasticamazon.model.dto.PublishersByLanguageResult;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SearchService {
    private final String index = "amazon_datascience_books";

    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public SearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public PublishersByLanguageResult findPublishersGroupedByLanguage() throws IOException {

        final String groupByLanguage = "group_by_language";
        final String groupByPublisher = "group_by_publisher";

        final Aggregation publishersSubAggregation = buildTermsAggregation("publisher");

        final Aggregation languageAggregation = buildTermsAggregationWithSubAggregation("language",
                groupByPublisher,
                publishersSubAggregation);

        final SearchRequest searchRequest = new SearchRequest.Builder()
                .index(index)
                .size(0)
                .aggregations(Map.of(groupByLanguage, languageAggregation))
                .build();

        final SearchResponse<Book> response = elasticsearchClient.search(searchRequest, Book.class);

        final StringTermsAggregate byLanguage = response.aggregations().get(groupByLanguage).sterms();
        final List<StringTermsBucket> aggregatedByLanguageBuckets = byLanguage.buckets().array();

        final PublishersByLanguageResult result = new PublishersByLanguageResult();

        aggregatedByLanguageBuckets.forEach(byLanguageBucket -> {
            final List<String> publishers = new ArrayList<>();
            final String language = byLanguageBucket.key().stringValue();
            log.info("Language: " + language);

            final StringTermsAggregate byPublisher = byLanguageBucket.aggregations().get(groupByPublisher).sterms();
            final List<StringTermsBucket> aggregatedByPublisherBuckets = byPublisher.buckets().array();

            aggregatedByPublisherBuckets.forEach(byPublisherBucket -> {
                final String publisher = byPublisherBucket.key().stringValue();
                publishers.add(publisher);
                log.info("Publisher: " + publisher);
                log.info("Count: " + byPublisherBucket.docCount());
            });

            log.info("=================================================");
            result.getPublishersByLanguage().put(language, publishers);
        });

        return result;
    }

    public AvgReviewsByPublisher getReviewsByPublisher() throws IOException {

        final String avgReviews = "avg_book_reviews_agg";
        final String groupByPublisher = "group_by_publisher";

        final Aggregation avgReviewsSubAggregation = buildAvgAggregation("avgReviews");

        final Aggregation publishersAggregation = buildTermsAggregationWithSubAggregation("publisher",
                avgReviews,
                avgReviewsSubAggregation);

        final SearchRequest searchRequest = new SearchRequest.Builder()
                .index(index)
                .size(0)
                .aggregations(Map.of(groupByPublisher, publishersAggregation))
                .build();
        final SearchResponse<Book> response = elasticsearchClient.search(searchRequest, Book.class);

        final StringTermsAggregate byPublisher = response.aggregations().get(groupByPublisher).sterms();
        final List<StringTermsBucket> aggregatedByPublisherBuckets = byPublisher.buckets().array();

        final AvgReviewsByPublisher result = new AvgReviewsByPublisher();

        aggregatedByPublisherBuckets.forEach(byPublisherBucket -> {
            final String publisher = byPublisherBucket.key().stringValue();
            log.info("Publisher: " + publisher);

            final AvgAggregate avgReviewAggregate = byPublisherBucket.aggregations().get(avgReviews).avg();
            log.info("Avg review score: " + avgReviewAggregate.value());

            result.getReviewsByPublisher().put(publisher, avgReviewAggregate.value());
            log.info("=================================================");
        });

        return result;
    }

    public BooksByPublisher getBooksFilteredByPagesWithMaxScoreByPublisher() throws IOException {
        final String maxScore = "maxStar5Score";
        final String groupByTitle = "group_by_title";
        final String groupByPublisher = "group_by_publisher";

        final Aggregation maxScoreSubAggregation = buildMaxAggregation("star5float");
        final Aggregation titleSubAggregation = buildTermsAggregation("title.keyword");

        final Query rangeQuery = new RangeQuery.Builder().field("pages").lt(JsonData.of(300)).build()._toQuery();

        final Aggregation publishersAggregation = new Aggregation.Builder()
                .terms(new TermsAggregation.Builder().field("publisher").build())
                .aggregations(Map.of(maxScore, maxScoreSubAggregation, groupByTitle, titleSubAggregation)).build();

        final SearchRequest searchRequest = new SearchRequest.Builder()
                .index(index)
                .size(0)
                .query(rangeQuery)
                .aggregations(groupByPublisher, publishersAggregation)
                .build();

        final SearchResponse<Book> response = elasticsearchClient.search(searchRequest, Book.class);

        final StringTermsAggregate byPublisher = response.aggregations().get(groupByPublisher).sterms();
        final List<StringTermsBucket> aggregatedByPublisherBuckets = byPublisher.buckets().array();

        final BooksByPublisher result = new BooksByPublisher();

        aggregatedByPublisherBuckets.forEach(byPublisherBucket -> {
            log.info(byPublisherBucket.toString());
            final String publisher = byPublisherBucket.key().stringValue();
            final Double maxStar5Score = byPublisherBucket.aggregations().get(maxScore).max().value();
            final List<String> titles = new ArrayList<>();
            final List<StringTermsBucket> titleBuckets = byPublisherBucket.aggregations().
                    get(groupByTitle).sterms().buckets().array();

            titleBuckets.forEach(titleBucket -> titles.add(titleBucket.key().stringValue()));
            final Map<Double, List<String>> titlesByScoreMap = Map.of(maxStar5Score, titles);

            result.getBooksByPublishers().put(publisher, titlesByScoreMap);

        });

        return result;
    }

    private Aggregation buildMaxAggregation(@NotNull final String fieldName) {
        return new Aggregation.Builder()
                .max(new MaxAggregation.Builder().field(fieldName).build())
                .build();
    }

    private Aggregation buildTermsAggregation(@NotNull final String fieldName) {
        return new Aggregation.Builder()
                .terms(new TermsAggregation.Builder().field(fieldName).size(100).build())
                .build();
    }

    private Aggregation buildAvgAggregation(@NotNull final String fieldName) {
        return new Aggregation.Builder()
                .avg(new AverageAggregation.Builder().field(fieldName).build())
                .build();
    }

    private Aggregation buildTermsAggregationWithSubAggregation(@NotNull final String fieldName,
                                                                @NotNull final String subAggregationName,
                                                                @NotNull final Aggregation subAggregation) {
        return new Aggregation.Builder()
                .terms(new TermsAggregation.Builder().field(fieldName).build())
                .aggregations(Map.of(subAggregationName, subAggregation)).build();
    }

}
