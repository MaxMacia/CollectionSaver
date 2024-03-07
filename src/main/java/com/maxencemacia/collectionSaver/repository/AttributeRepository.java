package com.maxencemacia.collectionSaver.repository;

import com.maxencemacia.collectionSaver.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
}
