package com.maxencemacia.collectionSaver.service;


public interface CollectionService {
    /**
     * Create a new Collection in the database
     * */
    String createCollection(String bodyString);
}
