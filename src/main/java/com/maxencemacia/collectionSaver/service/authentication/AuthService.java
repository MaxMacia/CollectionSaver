package com.maxencemacia.collectionSaver.service.authentication;

import com.maxencemacia.collectionSaver.entity.payload.authentication.request.LoginRequest;
import com.maxencemacia.collectionSaver.entity.payload.authentication.request.SignupRequest;
import com.maxencemacia.collectionSaver.entity.payload.authentication.response.JwtResponse;
import com.maxencemacia.collectionSaver.entity.payload.authentication.response.MessageResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    MessageResponse registerUser(SignupRequest signUpRequest);
}
