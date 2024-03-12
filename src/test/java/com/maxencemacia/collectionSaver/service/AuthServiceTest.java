package com.maxencemacia.collectionSaver.service;

import com.maxencemacia.collectionSaver.config.UserDetailsImpl;
import com.maxencemacia.collectionSaver.entity.authentication.ERole;
import com.maxencemacia.collectionSaver.entity.authentication.Role;
import com.maxencemacia.collectionSaver.entity.payload.authentication.request.LoginRequest;
import com.maxencemacia.collectionSaver.entity.payload.authentication.request.SignupRequest;
import com.maxencemacia.collectionSaver.entity.payload.authentication.response.JwtResponse;
import com.maxencemacia.collectionSaver.entity.payload.authentication.response.MessageResponse;
import com.maxencemacia.collectionSaver.exception.AppException;
import com.maxencemacia.collectionSaver.repository.RoleRepository;
import com.maxencemacia.collectionSaver.repository.UserRepository;
import com.maxencemacia.collectionSaver.service.authentication.impl.AuthServiceImpl;
import com.maxencemacia.collectionSaver.utils.JwtUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private AuthServiceImpl authService;
    @Test
    void authenticateUser() {
        //Given
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

        //When
        JwtResponse result = authService.authenticateUser(loginRequest);

        //Then
        assertThat(result.getToken()).isEqualTo(token);

        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
    }

    @Test
    void registerUser() {
        //Given
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

        //When
        MessageResponse result = authService.registerUser(signupRequest);

        //Then
        assertThat(result.getMessage()).isEqualTo("User registered successfully!");

        verify(userRepository, times(1)).existsByUsername(signupRequest.getUsername());
        verify(userRepository, times(1)).existsByEmail(signupRequest.getEmail());
        verify(encoder, times(1)).encode(signupRequest.getPassword());
        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
    }

    @Test
    void registerUser_UserNameAlreadyTaken() {
        //Given
        SignupRequest signupRequest = Instancio.create(SignupRequest.class);
        signupRequest.setPassword("123456");
        signupRequest.setEmail("max@email.com");
        signupRequest.setRole(Set.of("user"));

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(true);

        try {
            //When
            MessageResponse result = authService.registerUser(signupRequest);
            fail("An error was expected");
        } catch(AppException exception) {
            //Then
            assertThat(exception.getMessage()).isEqualTo("Username already taken");
        }
    }

    @Test
    void registerUser_EmailAlreadyTaken() {
        //Given
        SignupRequest signupRequest = Instancio.create(SignupRequest.class);
        signupRequest.setPassword("123456");
        signupRequest.setEmail("max@email.com");
        signupRequest.setRole(Set.of("user"));

        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        try {
            //When
            MessageResponse result = authService.registerUser(signupRequest);
            fail("An error was expected");
        } catch(AppException exception) {
            //Then
            assertThat(exception.getMessage()).isEqualTo("Email already taken");
        }
    }
}
