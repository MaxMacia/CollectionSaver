package com.maxencemacia.collectionSaver.controller;

import org.instancio.Instancio;
import org.json.JSONObject;
import com.maxencemacia.collectionSaver.exception.AppException;
import com.maxencemacia.collectionSaver.exception.Error;
import com.maxencemacia.collectionSaver.service.CollectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class CollectionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    CollectionService collectionService;
    @Test
    void getCollections() throws Exception {
        String result = "[{\"type\":\"anyType\",\"string\":\"anyString\",\"number\":30}]";

        when(collectionService.getCollections()).thenReturn(result);

        mockMvc.perform(
                get("/api/collections")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").exists());
    }
    @Test
    void getCollections_empty() throws Exception {
        String result = "[]";

        when(collectionService.getCollections()).thenReturn(result);

        mockMvc.perform(
                        get("/api/collections")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").doesNotExist());
    }
    @Test
    void getOneCollection() throws Exception {
        String result = "{\"type\":\"anyType\",\"string\":\"anyString\",\"number\":30}";

        when(collectionService.getOneCollection(1L)).thenReturn(result);

        mockMvc.perform(
                        get("/api/collections/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").exists());
    }
    @Test
    void getOneCollection_CollectionNotFound() throws Exception {
        when(collectionService.getOneCollection(1L)).thenThrow(new AppException(Error.COLLECTION_NOT_FOUND));

        mockMvc.perform(
                        get("/api/collections/1")
                )
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(username = "User")
    void createCollection() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "anyType");
        jsonObject.put("string", "anyString");
        jsonObject.put("number", 30);

        mockMvc.perform(
                post("/api/collections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").exists());
    }

    @Test
    @WithMockUser(username = "User")
    void createCollection_MustHaveAType() throws Exception {
        JSONObject jsonObject = Instancio.create(JSONObject.class);
        jsonObject.put("string", "anyString");
        jsonObject.put("number", 30);

        mockMvc.perform(
                        post("/api/collections")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObject.toString())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}
