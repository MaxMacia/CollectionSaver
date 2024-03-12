package com.maxencemacia.collectionSaver.controller;

import com.maxencemacia.collectionSaver.entity.authentication.ERole;
import com.maxencemacia.collectionSaver.entity.authentication.Role;
import com.maxencemacia.collectionSaver.entity.authentication.User;
import com.maxencemacia.collectionSaver.repository.UserRepository;
import com.maxencemacia.collectionSaver.utils.WithMockCustomUser;
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


import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
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
    @MockBean
    UserRepository userRepository;
    @Test
    @WithMockUser(username = "User")
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
    @WithMockUser(username = "User")
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
    @WithMockUser(username = "User")
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
    @WithMockUser(username = "User")
    void getOneCollection_CollectionNotFound() throws Exception {
        when(collectionService.getOneCollection(1L)).thenThrow(new AppException(Error.COLLECTION_NOT_FOUND));

        mockMvc.perform(
                        get("/api/collections/1")
                )
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockCustomUser
    void createCollection() throws Exception {
        User user = User.builder()
                .id(1L)
                .uuid("1")
                .username("user")
                .email("user@mail.com")
                .password("password")
                .roles(Set.of(new Role(ERole.ROLE_USER)))
                .build();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "anyType");
        jsonObject.put("string", "anyString");
        jsonObject.put("number", 30);

        when(userRepository.findByUuid(any())).thenReturn(Optional.of(user));

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
