package com.maxencemacia.collectionSaver.service;


import com.maxencemacia.collectionSaver.entity.authentication.User;

public interface CollectionService {
    /**
     * Get all the Collections from database
     * */
    String getCollections();
    /**
     * Get one Collection from database
     * */
    String getOneCollection(Long id);
    /**
     * Create a new Collection in the database
     * */
    String createCollection(User user, String bodyString);
}
