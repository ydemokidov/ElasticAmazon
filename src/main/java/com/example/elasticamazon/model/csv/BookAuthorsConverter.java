package com.example.elasticamazon.model.csv;

import com.opencsv.bean.AbstractBeanField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookAuthorsConverter<T,I> extends AbstractBeanField<T,I> {

    @Override
    protected Object convert(String value) {
        final List<String> authors = new ArrayList<>();
        if(value.contains(", ")) {
            final String[] authorParts = value.replace("[ ", "").replace("]", "").split(", ");
            System.out.println(authorParts.length);
            Arrays.stream(authorParts).sequential().filter(s -> Character.isUpperCase(s.charAt(0))).forEach(authors::add);
        }else{
            authors.add(value.replace("[ ", "").replace("]", ""));
        }
        return authors;
    }

}
