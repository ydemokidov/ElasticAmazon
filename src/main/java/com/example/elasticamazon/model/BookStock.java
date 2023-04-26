package com.example.elasticamazon.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
public class BookStock {

    @CsvBindByName(column = "ISBN_13")
    private String ISBN13;

    @CsvBindByName
    @Field(type = FieldType.Integer)
    private Integer cityId;

    @CsvBindByName(column = "stockNr")
    @Field(type = FieldType.Integer)
    private Integer stockNumber;

}
