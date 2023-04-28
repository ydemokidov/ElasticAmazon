package com.example.elasticamazon.model;

import com.example.elasticamazon.model.csv.BookAuthorsConverter;
import com.example.elasticamazon.model.csv.BookPagesConverter;
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
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Document(indexName = "amazon_datascience_books")
@Setting(settingPath = "/settings.json")
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

    @Field(type = FieldType.Keyword)
    @CsvBindByName
    String publisher;

    @Field
    @CsvBindByName(column = "ISBN_13")
    String ISBN13;

    @CsvBindByName
    String link;

    @CsvBindByName(column = "complete_link")
    String completeLink;

    @Field(type = FieldType.Float)
    Float star5float;

    @Field(type = FieldType.Float)
    Float star4float;

    @Field(type = FieldType.Float)
    Float star3float;

    @Field(type = FieldType.Float)
    Float star2float;

    @Field(type = FieldType.Float)
    Float star1float;

    @Field(type = FieldType.Object)
    List<BookStock> bookStocksData = new ArrayList<>();

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
