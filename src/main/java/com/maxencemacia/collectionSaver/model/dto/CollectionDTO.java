package com.maxencemacia.collectionSaver.model.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public class CollectionDTO {
    String name;
    Map<String, Object> attributes;
}
