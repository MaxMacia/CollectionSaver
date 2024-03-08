package com.maxencemacia.collectionSaver.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxencemacia.collectionSaver.exception.AppException;
import com.maxencemacia.collectionSaver.exception.Error;
import com.maxencemacia.collectionSaver.model.Attribute;
import com.maxencemacia.collectionSaver.model.Collection;
import com.maxencemacia.collectionSaver.repository.AttributeRepository;
import com.maxencemacia.collectionSaver.repository.CollectionRepository;
import com.maxencemacia.collectionSaver.service.CollectionService;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private CollectionRepository collectionRepository;
    private AttributeRepository attributeRepository;

    @Override
    public String getCollections() {
        List<Collection> collections = collectionRepository.findAll();
        JSONArray jsonArrayCollections = new JSONArray();

        for (Collection collection : collections) {
            JSONObject jsonObjectCollection = makeJSONobjectCollection(collection);
            jsonArrayCollections.put(jsonObjectCollection);
        }

        return jsonArrayCollections.toString();
    }
    @Override
    public String getOneCollection(Long id) {
        Collection collection = collectionRepository.findById(id).orElseThrow(
                () -> new AppException(Error.COLLECTION_NOT_FOUND)
        );
        JSONObject jsonObjectCollection = makeJSONobjectCollection(collection);

        return jsonObjectCollection.toString();
    }
    private JSONObject makeJSONobjectCollection(Collection collection) {
        JSONObject jsonObjectAttributes = new JSONObject();
        JSONObject jsonObjectCollection = new JSONObject();
        List<Attribute> attributes = collection.getAttributes();
        for (Attribute attribute : attributes) {
            if (attribute.getStringValue() != null) {
                jsonObjectAttributes.put(attribute.getName(), attribute.getStringValue());
            } else if (attribute.getIntValue() != null) {
                jsonObjectAttributes.put(attribute.getName(), attribute.getIntValue());
            } else if (attribute.getDoubleValue() != null) {
                jsonObjectAttributes.put(attribute.getName(), attribute.getDoubleValue());
            } else {
                jsonObjectAttributes.put(attribute.getName(), attribute.getBoolValue());
            }
        }
        jsonObjectCollection.put("type", collection.getType());
        jsonObjectCollection.put("attributes", jsonObjectAttributes);

        return jsonObjectCollection;
    }

    @Override
    public String createCollection(String bodyString) {
        Collection collection;
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject jsonObjectAttributes = new JSONObject();
        JSONObject jsonObjectCollection = new JSONObject();

        try {
            JsonNode jsonNode = objectMapper.readTree(bodyString);

            if (jsonNode.has("type")) {
                List<String> fieldNames = new ArrayList<>();
                List<Attribute> attributes = new ArrayList<>();
                Iterator<String> itr = jsonNode.fieldNames();

                while (itr.hasNext()) {
                    fieldNames.add(itr.next());
                }

                fieldNames.remove("type");

                for (String fieldName : fieldNames) {
                    Integer intValue = null;
                    Double doubleValue = null;
                    String stringValue = null;
                    Boolean boolValue = null;
                    JsonNode node = jsonNode.get(fieldName);

                    if (node.isInt()) {
                        intValue = node.asInt();
                        jsonObjectAttributes.put(fieldName, intValue);
                    } else if (node.isDouble()) {
                        doubleValue = node.asDouble();
                        jsonObjectAttributes.put(fieldName, doubleValue);
                    } else if (node.isTextual()) {
                        stringValue = node.asText();
                        jsonObjectAttributes.put(fieldName, stringValue);
                    } else {
                        boolValue = node.booleanValue();
                        jsonObjectAttributes.put(fieldName, boolValue);
                    }
                    attributes.add(
                            Attribute.builder()
                                    .name(fieldName)
                                    .intValue(intValue)
                                    .doubleValue(doubleValue)
                                    .stringValue(stringValue)
                                    .boolValue(boolValue)
                                    .build()
                    );
                }

                collection = Collection.builder()
                        .type(jsonNode.get("type").asText())
                        .attributes(attributes)
                        .build();

                collectionRepository.save(collection);
                Collection finalCollection = collection;
                collection.getAttributes().forEach(attribute -> attribute.setCollection(finalCollection));
                attributeRepository.saveAll(collection.getAttributes());
            } else {
                throw new AppException(Error.MUST_HAVE_A_TYPE);
            }
        } catch (JsonProcessingException e) {
            throw new AppException(Error.BAD_REQUEST);
        }

        jsonObjectCollection.put("type", collection.getType());
        jsonObjectCollection.put("attributes", jsonObjectAttributes);

        return jsonObjectCollection.toString();
    }
}
