package com.example.elasticamazon.model;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class BookPagesConverter<T,I> extends AbstractBeanField<T,I> {

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try{
            if(value.isEmpty()) return null;
            return Math.round(Float.parseFloat(value));
        }catch (NumberFormatException e){
            e.printStackTrace();
            return null;
        }
    }

}