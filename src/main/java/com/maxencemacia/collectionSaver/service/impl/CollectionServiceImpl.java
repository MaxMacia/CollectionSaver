package com.maxencemacia.collectionSaver.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxencemacia.collectionSaver.model.Attribute;
import com.maxencemacia.collectionSaver.model.Collection;
import com.maxencemacia.collectionSaver.model.dto.CollectionDTO;
import com.maxencemacia.collectionSaver.repository.AttributeRepository;
import com.maxencemacia.collectionSaver.repository.CollectionRepository;
import com.maxencemacia.collectionSaver.service.CollectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private CollectionRepository collectionRepository;
    private AttributeRepository attributeRepository;
    @Override
    public CollectionDTO createCollection(String bodyString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Collection collection = null;
        Map<String, Object> attributeMap = new HashMap<>();
        try {
            JsonNode jsonNode = objectMapper.readTree(bodyString);

            if (jsonNode.has("name")) {
                List<String> fieldNames = new ArrayList<>();
                List<Attribute> attributes = new ArrayList<>();
                Iterator<String> itr = jsonNode.fieldNames();

                while (itr.hasNext()) {
                    fieldNames.add(itr.next());
                }

                fieldNames.remove("name");

                for (String fieldName : fieldNames) {
                    Integer numberValue = null;
                    String stringValue = null;
                    JsonNode node = jsonNode.get(fieldName);

                    if (node.isNumber()) {
                        numberValue = node.asInt();
                        attributeMap.put(fieldName, numberValue);
                    } else {
                        stringValue = node.asText();
                        attributeMap.put(fieldName, stringValue);
                    }
                    attributes.add(
                            Attribute.builder()
                                    .name(fieldName)
                                    .numberValue(numberValue)
                                    .stringValue(stringValue)
                                    .build()
                    );
                }

                collection = Collection.builder()
                        .name(jsonNode.get("name").asText())
                        .attributes(attributes)
                        .build();
                collectionRepository.save(collection);
                Collection finalCollection = collection;
                collection.getAttributes().forEach(attribute -> attribute.setCollection(finalCollection));
                attributeRepository.saveAll(collection.getAttributes());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assert collection != null;
        return CollectionDTO.builder()
                .name(collection.getName())
                .attributes(attributeMap)
                .build();
    }
}
