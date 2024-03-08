package com.maxencemacia.collectionSaver.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/collections")
public interface CollectionApi {
    /**
     * Create a new Collection in the database
     * */
    @PostMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createCollection(@RequestBody String bodyString);
}
