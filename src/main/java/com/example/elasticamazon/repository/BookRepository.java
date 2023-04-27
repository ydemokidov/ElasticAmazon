package com.example.elasticamazon.repository;

import com.example.elasticamazon.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {
    Optional<Book> findByISBN13(String isbn13);

}