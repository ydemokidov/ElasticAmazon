package com.example.elasticamazon.controller;

import com.example.elasticamazon.model.dto.AvgReviewsByPublisher;
import com.example.elasticamazon.model.dto.BooksByPublisher;
import com.example.elasticamazon.model.dto.PublishersByLanguageResult;
import com.example.elasticamazon.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/publishers")
    public PublishersByLanguageResult findPublisherByLanguage() throws IOException {
        return searchService.findPublishersGroupedByLanguage();
    }

    @GetMapping("/reviews")
    public AvgReviewsByPublisher findReviewsByPublisher() throws IOException {
        return searchService.getReviewsByPublisher();
    }

    @GetMapping("/books")
    public BooksByPublisher getBooksFilteredByPagesWithMaxScoreByPublisher() throws IOException {
        return searchService.getBooksFilteredByPagesWithMaxScoreByPublisher();
    }

}
