package com.example.elasticamazon.repository;

import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.transform.Settings;
import com.example.elasticamazon.model.Book;
import org.elasticsearch.client.RequestOptions;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {



}