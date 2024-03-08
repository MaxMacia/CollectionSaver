package com.maxencemacia.collectionSaver.controller.impl;

import com.maxencemacia.collectionSaver.controller.CollectionApi;
import com.maxencemacia.collectionSaver.service.CollectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CollectionController implements CollectionApi {
    private CollectionService collectionService;
    @Override
    public ResponseEntity<String> createCollection(String bodyString) {
        return new ResponseEntity<>(collectionService.createCollection(bodyString), HttpStatus.CREATED);
    }
}
