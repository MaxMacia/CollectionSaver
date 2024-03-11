package com.maxencemacia.collectionSaver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


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
    @Autowired
    private ObjectMapper objectMapper;

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
