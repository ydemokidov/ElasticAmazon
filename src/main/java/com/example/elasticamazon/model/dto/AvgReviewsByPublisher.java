package com.example.elasticamazon.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class AvgReviewsByPublisher {

    private Map<String,Float> reviewsByPublisher = new HashMap<>();

}
