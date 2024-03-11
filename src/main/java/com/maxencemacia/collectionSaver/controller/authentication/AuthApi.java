package com.maxencemacia.collectionSaver.controller.authentication;

import com.maxencemacia.collectionSaver.entity.payload.authentication.request.LoginRequest;
import com.maxencemacia.collectionSaver.entity.payload.authentication.request.SignupRequest;
import com.maxencemacia.collectionSaver.entity.payload.authentication.response.JwtResponse;
import com.maxencemacia.collectionSaver.entity.payload.authentication.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication")
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "${spring.cors.url}", maxAge = 3600)
public interface AuthApi {
    @PostMapping("/login")
    @Operation(summary="Login")
    @ApiResponses(value ={
            @ApiResponse(   responseCode = "200" ,
                    description = "Return the user logged in with their token",
                    content = {@Content( mediaType = "application/json")})
    })
    ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);
    @PostMapping("/signup")
    @Operation(summary="Signup")
    @ApiResponses(value ={
            @ApiResponse(   responseCode = "201" ,
                    description = "Creates a user",
                    content = {@Content( mediaType = "application/json")})
    })
    ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest);
}
