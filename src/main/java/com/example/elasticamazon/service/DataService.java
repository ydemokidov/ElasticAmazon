package com.example.elasticamazon.service;

import com.example.elasticamazon.model.Book;
import com.example.elasticamazon.model.BookStock;
import com.example.elasticamazon.repository.BookRepository;
import com.google.common.collect.Iterators;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class DataService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    private final int batchSize = 100;
    private final BookRepository bookRepository;
    @Value("classpath:final_book_dataset_kaggle2.csv")
    private Resource datafileResource;

    @Value("classpath:kagle_book_stocks.csv")
    private Resource stockDatafileResource;

    @Autowired
    public DataService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void populateData(){
        processBooksCsv();
        processBookStockCsv();
    }

    private void updateBookStocksData(@NotNull final BookStock bookStock){
        executorService.submit(()->{
            Optional<Book> bookOptional = bookRepository.findByISBN13(bookStock.getISBN13());
            if (bookOptional.isPresent()) {
                final Book book = bookOptional.get();
                book.getBookStocksData().add(bookStock);
                bookRepository.save(book);
            }
        });
    }

    private void processBooksCsv(){
        try(final InputStream is = datafileResource.getInputStream();
            final InputStreamReader isReader = new InputStreamReader(is);
            final BufferedReader bufferedReader = new BufferedReader(isReader)){
            final CsvToBean<Book> csvToBean  = new CsvToBeanBuilder<Book>(bufferedReader)
                    .withType(Book.class)
                    .withSeparator(',')
                    .withQuoteChar('\"')
                    .build();
            Iterators.partition(csvToBean.stream().iterator(), batchSize)
                    .forEachRemaining(bookRepository::saveAll);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processBookStockCsv(){
        try(final InputStream is = stockDatafileResource.getInputStream();
            final InputStreamReader isReader = new InputStreamReader(is);
            final BufferedReader bufferedReader = new BufferedReader(isReader)){
            final CsvToBean<BookStock> csvToBean  = new CsvToBeanBuilder<BookStock>(bufferedReader)
                    .withType(BookStock.class)
                    .withSeparator(',')
                    .build();
            csvToBean.stream().forEach(this::updateBookStocksData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
