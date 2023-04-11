package com.example.elasticamazon.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Document(indexName = "amazon_datascience_books")
public class Book implements Serializable {

    @Field
    @CsvCustomBindByName(column = "author",converter = BookAuthorsConverter.class)
    List<String> authors;

    @Id
    @CsvBindByName
    String title;

    @Field
    @CsvBindByName
    Float price;
    @Field
    @CsvCustomBindByName(column = "pages",converter = BookPagesConverter.class)
    Integer pages;

    @Field
    @CsvBindByName(column = "avg_reviews")
    Float avgReviews;

    @Field
    @CsvBindByName
    String star5;

    @Field
    @CsvBindByName
    String star4;

    @Field
    @CsvBindByName
    String star3;

    @Field
    @CsvBindByName
    String star2;

    @Field
    @CsvBindByName
    String star1;

    @Field
    @CsvBindByName
    String dimensions;

    @Field
    @CsvBindByName
    String weight;

    @CsvBindByName
    @Field(type = FieldType.Keyword)
    String language;

    @Field
    @CsvBindByName
    String publisher;

    @Field(type = FieldType.Keyword)
    @CsvBindByName(column = "ISBN_13")
    String ISBN13;

    @CsvBindByName
    String link;

    @CsvBindByName(column = "complete_link")
    String completeLink;

    @Override
    public String toString() {
        return "Book{" +
                "authors='" + authors + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", pages=" + pages +
                ", avgReviews=" + avgReviews +
                ", star5='" + star5 + '\'' +
                ", star4='" + star4 + '\'' +
                ", star3='" + star3 + '\'' +
                ", star2='" + star2 + '\'' +
                ", star1='" + star1 + '\'' +
                ", dimensions='" + dimensions + '\'' +
                ", weight='" + weight + '\'' +
                ", language='" + language + '\'' +
                ", publisher='" + publisher + '\'' +
                ", ISBN13='" + ISBN13 + '\'' +
                ", link='" + link + '\'' +
                ", completeLink='" + completeLink + '\'' +
                '}';
    }
}
