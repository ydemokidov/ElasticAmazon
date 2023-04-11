package com.example.elasticamazon.service;

import com.example.elasticamazon.model.Book;
import com.example.elasticamazon.repository.BookRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    private final BookRepository bookRepository;
    @Value("classpath:final_book_dataset_kaggle2.csv")
    private Resource datafileResource;

    @Autowired
    public DataService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void populateData(){
        bookRepository.saveAll(parseCsv());
    }

    private List<Book> parseCsv(){
        final List<Book> books = new ArrayList<>();
        try(final InputStream is = datafileResource.getInputStream();
            final InputStreamReader isReader = new InputStreamReader(is);
            final BufferedReader bufferedReader = new BufferedReader(isReader)){
            final CsvToBean<Book> csvToBean  = new CsvToBeanBuilder<Book>(bufferedReader)
                    .withType(Book.class)
                    .withSeparator(',')
                    .withQuoteChar('\"')
                    .build();
            csvToBean.stream().forEach(books::add);
            books.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

}
