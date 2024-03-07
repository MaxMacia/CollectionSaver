package com.maxencemacia.collectionSaver.repository;

import com.maxencemacia.collectionSaver.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
