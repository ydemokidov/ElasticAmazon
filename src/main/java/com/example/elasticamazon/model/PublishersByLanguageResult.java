package com.example.elasticamazon.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PublishersByLanguageResult {

    private String language;

    private List<String> publishers = new ArrayList<>();

}
