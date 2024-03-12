package com.maxencemacia.collectionSaver.controller;

import com.maxencemacia.collectionSaver.entity.authentication.User;
import com.maxencemacia.collectionSaver.utils.CurrentUser;
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
    @Operation(summary="Get all collections")
    @ApiResponses(value ={
            @ApiResponse(   responseCode = "200" ,
                    description = "Get all collections",
                    content = {@Content( mediaType = "application/json")})
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<String> getCollections();

    /**
     * Get one Collection by id from database
     * */
    @GetMapping("/{id}")
    @Operation(summary="Get one collection by id")
    @ApiResponses(value ={
            @ApiResponse(   responseCode = "200" ,
                    description = "Get one collection by id",
                    content = {@Content( mediaType = "application/json")})
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
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
    ResponseEntity<String> createCollection(@CurrentUser User user, @RequestBody String bodyString);
}
