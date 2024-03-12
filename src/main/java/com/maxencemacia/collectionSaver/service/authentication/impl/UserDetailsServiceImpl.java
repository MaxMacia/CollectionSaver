package com.maxencemacia.collectionSaver.service.authentication.impl;

import com.maxencemacia.collectionSaver.config.UserDetailsImpl;
import com.maxencemacia.collectionSaver.exception.AppException;
import com.maxencemacia.collectionSaver.exception.Error;
import com.maxencemacia.collectionSaver.entity.authentication.User;
import com.maxencemacia.collectionSaver.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));

        return UserDetailsImpl.build(user);
    }

    public User getCurrentUser() {
        String userUuid = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid();
        Optional<User> optional = userRepository.findByUuid(userUuid);

        if (optional.isEmpty()) {
            log.error("Utilisateur non trouvé pour l'uid suivant : {}", userUuid);
            throw new AppException(Error.USER_NOT_FOUND);
        }

        return optional.get();
    }
}
