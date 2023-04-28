package com.example.elasticamazon.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.elasticamazon.model.Book;
import com.example.elasticamazon.model.dto.PublishersByLanguageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders.terms;
import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.queryString;

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

        final Map<String, Aggregation> aggregationsMap = new HashMap<>();

        final Aggregation publishersSubAggregation = new Aggregation.Builder()
                .terms(new TermsAggregation.Builder().field("publisher").build())
                .build();

        final Aggregation languageAggregation = new Aggregation.Builder()
                .terms(new TermsAggregation.Builder().field("language").build())
                .aggregations(new HashMap<>() {{
                    put(groupByPublisher, publishersSubAggregation);
                }}).build();

        aggregationsMap.put(groupByLanguage, languageAggregation);

        final SearchRequest searchRequest = new SearchRequest.Builder()
                .index(index)
                .size(0)
                .aggregations(aggregationsMap)
                .build();

        final SearchResponse<Book> response = elasticsearchClient.search(searchRequest, Book.class);

        final StringTermsAggregate byLanguage = response.aggregations().get(groupByLanguage).sterms();
        final List<StringTermsBucket> aggregatedByLanguageBuckets = byLanguage.buckets().array();

        final PublishersByLanguageResult result = new PublishersByLanguageResult();

        aggregatedByLanguageBuckets.forEach(byLanguageBucket -> {
            final List<String> publishers = new ArrayList<>();
            final String language = byLanguageBucket.key().stringValue();
            log.info("Language: " + language);
            final StringTermsAggregate byPublisher= byLanguageBucket.aggregations().get(groupByPublisher).sterms();
            final List<StringTermsBucket> aggregatedByPublisherBuckets = byPublisher.buckets().array();
            aggregatedByPublisherBuckets.forEach(byPublisherBucket ->{
                final String publisher = byPublisherBucket.key().stringValue();
                publishers.add(publisher);
                log.info("Publisher: "+ publisher);
                log.info("Count: " + byPublisherBucket.docCount());
            });
            log.info("=================================================");
            result.getPublishersByLanguage().put(language,publishers);
        });

        return result;
    }



}
