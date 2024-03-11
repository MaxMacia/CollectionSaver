package com.maxencemacia.collectionSaver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/collections")
public interface CollectionApi {
    /**
     * Get all the Collections from database
     * */
    @GetMapping
    ResponseEntity<String> getCollections();
    /**
     * Get one Collection from database
     * */
    @GetMapping("/{id}")
    ResponseEntity<String> getOneCollection(@PathVariable Long id);
    /**
     * Create a new Collection in the database
     * */
    @PostMapping
    ResponseEntity<String> createCollection(@RequestBody String bodyString);
}
