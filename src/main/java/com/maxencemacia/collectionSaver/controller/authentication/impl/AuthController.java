package com.maxencemacia.collectionSaver.controller.authentication.impl;

import com.maxencemacia.collectionSaver.controller.authentication.AuthApi;
import com.maxencemacia.collectionSaver.entity.payload.authentication.request.LoginRequest;
import com.maxencemacia.collectionSaver.entity.payload.authentication.request.SignupRequest;
import com.maxencemacia.collectionSaver.entity.payload.authentication.response.JwtResponse;
import com.maxencemacia.collectionSaver.entity.payload.authentication.response.MessageResponse;
import com.maxencemacia.collectionSaver.service.authentication.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController implements AuthApi {
    AuthService authService;

    @Override
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @Override
    public ResponseEntity<MessageResponse> registerUser(SignupRequest signUpRequest) {
        return new ResponseEntity<>(authService.registerUser(signUpRequest), HttpStatus.CREATED);
    }
}
