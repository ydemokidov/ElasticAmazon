package com.example.elasticamazon.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders.terms;
import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.queryString;

@Service
public class SearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public SearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public void findPublishersGroupedByLanguage() throws IOException {

        /*

            Map<String, Aggregation> map = new HashMap<>();

    Aggregation subAggregation = new Aggregation.Builder()
        .avg(new AverageAggregation.Builder().field("revenue").build())
        .build();

    Aggregation aggregation = new Aggregation.Builder()
        .terms(new TermsAggregation.Builder().field("director.keyword").build())
        .aggregations(new HashMap<>() {{
          put("avg_renevue", subAggregation);
        }}).build();

    map.put("agg_director", aggregation);

    SearchRequest searchRequest = new SearchRequest.Builder()
        .index("idx_name")
        .size(0)
        .aggregations(map)
        .build();
         */

    }
}
