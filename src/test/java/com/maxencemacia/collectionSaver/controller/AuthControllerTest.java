package com.maxencemacia.collectionSaver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxencemacia.collectionSaver.config.UserDetailsImpl;
import com.maxencemacia.collectionSaver.entity.authentication.ERole;
import com.maxencemacia.collectionSaver.entity.authentication.Role;
import com.maxencemacia.collectionSaver.entity.payload.authentication.request.LoginRequest;
import com.maxencemacia.collectionSaver.entity.payload.authentication.request.SignupRequest;
import com.maxencemacia.collectionSaver.repository.RoleRepository;
import com.maxencemacia.collectionSaver.repository.UserRepository;
import com.maxencemacia.collectionSaver.utils.JwtUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.instancio.Select.all;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private PasswordEncoder encoder;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;

    @Test
    void authenticateUser() throws Exception {
        LoginRequest loginRequest = Instancio.create(LoginRequest.class);
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username(loginRequest.getUsername())
                .password(loginRequest.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(ERole.ROLE_USER.name())))
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword());
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXgiLCJpYXQiOjE3MTAxNjcyODMsImV4cCI6MTcxMDE3MDg4M30.0FqPo7vsR2sGE_IhTdHbn1zCKRQehEP_GuoF7SR9-1w";

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void registerUser() throws Exception {
        SignupRequest signupRequest = Instancio.create(SignupRequest.class);
        signupRequest.setPassword("123456");
        signupRequest.setEmail("max@email.com");
        signupRequest.setRole(Set.of("user"));
        Role userRole = Role.builder().name(ERole.ROLE_USER).build();
        String encodedPassword = "$2a$10$bh8MsRE2qSQll2DR6h05mu3VWwvVNcNJzJOZv3bxWPJukBPACX/su";

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(encoder.encode(signupRequest.getPassword())).thenReturn(encodedPassword);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.ofNullable(userRole));

        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signupRequest))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void registerUser_UserNameAlreadyTaken() throws Exception {
        SignupRequest signupRequest = Instancio.create(SignupRequest.class);
        signupRequest.setPassword("123456");
        signupRequest.setEmail("max@email.com");
        signupRequest.setRole(Set.of("user"));

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(true);

        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signupRequest))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void registerUser_EmailAlreadyTaken() throws Exception {
        SignupRequest signupRequest = Instancio.create(SignupRequest.class);
        signupRequest.setPassword("123456");
        signupRequest.setEmail("max@email.com");
        signupRequest.setRole(Set.of("user"));

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signupRequest))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());
    }
}
