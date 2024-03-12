package com.maxencemacia.collectionSaver.service;

import com.maxencemacia.collectionSaver.entity.authentication.User;
import com.maxencemacia.collectionSaver.exception.AppException;
import com.maxencemacia.collectionSaver.repository.UserRepository;
import com.maxencemacia.collectionSaver.service.authentication.impl.UserDetailsServiceImpl;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @Test
    void loadUserByUsername() {
        //Given
        User user = Instancio.create(User.class);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //When
        UserDetails result = userDetailsService.loadUserByUsername(user.getUsername());

        //Then
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());

        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        //Given
        User user = Instancio.create(User.class);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        try {
            //When
            UserDetails result = userDetailsService.loadUserByUsername(user.getUsername());
            fail("An error was expected");
        } catch(AppException appException) {
            //Then
            assertThat(appException.getMessage()).isEqualTo("User not found");
        }
    }
}
