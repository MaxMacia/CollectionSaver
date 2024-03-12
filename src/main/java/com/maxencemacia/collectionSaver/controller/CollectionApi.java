package com.maxencemacia.collectionSaver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/collections")
public interface CollectionApi {
    /**
     * Get all the Collections from database
     * */
    @GetMapping
    ResponseEntity<String> getCollections();
    /**
     * Get one Collection from database
     * */
    @GetMapping("/{id}")
    ResponseEntity<String> getOneCollection(@PathVariable Long id);
    /**
     * Create a new Collection in the database
     * */
    @PostMapping
    @Operation(summary="Create a new collection")
    @ApiResponses(value ={
            @ApiResponse(   responseCode = "201" ,
                    description = "Create one new collection",
                    content = {@Content( mediaType = "application/json")})
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<String> createCollection(@RequestBody String bodyString);
}
