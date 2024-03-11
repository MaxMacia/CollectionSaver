package com.maxencemacia.collectionSaver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxencemacia.collectionSaver.exception.AppException;
import com.maxencemacia.collectionSaver.entity.Attribute;
import com.maxencemacia.collectionSaver.entity.Collection;
import com.maxencemacia.collectionSaver.repository.AttributeRepository;
import com.maxencemacia.collectionSaver.repository.CollectionRepository;
import com.maxencemacia.collectionSaver.service.impl.CollectionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CollectionServiceTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private CollectionRepository collectionRepository;
    @Mock
    private AttributeRepository attributeRepository;
    @InjectMocks
    private CollectionServiceImpl collectionService;
    @Test
    void createCollection() throws JsonProcessingException {
        //Given
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("type", "anyType");
        jsonNode.put("string", "anyString");
        jsonNode.put("number", 30);

        String bodyString = "{\"type\":\"anyType\",\"string\":\"anyString\",\"number\":30}";

        Collection collection = Collection.builder()
                .id(1L)
                .type("anyType")
                .attributes(
                        List.of(
                                Attribute.builder()
                                        .id(1L)
                                        .name("string")
                                        .stringValue("anyString")
                                        .build(),
                                Attribute.builder()
                                        .id(2L)
                                        .name("number")
                                        .intValue(30)
                                        .build()
                        )
                )
                .build();

        when(objectMapper.readTree(bodyString)).thenReturn(jsonNode);
        when(collectionRepository.save(any())).thenReturn(collection);
        when(attributeRepository.saveAll(any())).thenReturn(collection.getAttributes());

        //When
        String result = collectionService.createCollection(bodyString);

        //Then
        assertThat(result).isEqualTo("{\"attributes\":{\"number\":30,\"string\":\"anyString\"},\"type\":\"anyType\"}");

        //verify(objectMapper, times(1)).readTree(bodyString);
        //verify(collectionRepository, times(1)).save(collection);
        //verify(attributeRepository, times(1)).saveAll(collection.getAttributes());
    }

    @Test
    void createCollection_MustHaveAType() throws JsonProcessingException {
        //Given
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("string", "anyString");
        jsonNode.put("number", 30);

        String bodyString = "{\"string\":\"anyString\",\"number\":30}";


        when(objectMapper.readTree(bodyString)).thenReturn(jsonNode);

        try {
            //When
            collectionService.createCollection(bodyString);
            fail("An error was expected");
        } catch (AppException appException) {
            //Then
            assertThat(appException.getMessage()).isEqualTo("The collection must have a type");
        }
    }
}
