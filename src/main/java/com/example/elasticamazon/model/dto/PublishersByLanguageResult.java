package com.example.elasticamazon.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PublishersByLanguageResult {

    private Map<String, List<String>> publishersByLanguage = new HashMap<>();

}
