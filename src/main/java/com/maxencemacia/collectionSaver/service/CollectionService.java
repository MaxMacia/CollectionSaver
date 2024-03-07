package com.maxencemacia.collectionSaver.service;

import com.maxencemacia.collectionSaver.model.dto.CollectionDTO;

public interface CollectionService {
    /**
     * Create a new Collection in the database
     * */
    CollectionDTO createCollection(String bodyString);
}
