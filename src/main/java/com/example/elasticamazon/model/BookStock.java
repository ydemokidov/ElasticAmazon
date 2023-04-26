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

    @Field
    @CsvBindByName(column = "ISBN_13")
    private String ISBN_13;

    @Field(type = FieldType.Integer)
    @CsvBindByName(column = "cityId")
    private Integer cityId;

    @Field(type = FieldType.Integer)
    @CsvBindByName(column = "stockNr")
    private Integer stockNumber;

}
