package com.uriencedric.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.uriencedric.authorization.common.exception.AuthException;
import com.uriencedric.authorization.persistence.entity.User;
import com.uriencedric.authorization.persistence.entity.UserCredentials;
import com.uriencedric.authorization.persistence.repository.CredentialRepository;
import com.uriencedric.authorization.persistence.repository.UserRepository;
import com.uriencedric.authorization.service.authProfiles.UserProfile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findOneByUsername(username);
        if (user == null) {
            log.error(String.format("User %s not found", username));
            throw new AuthException("User not found");
        } else if (user.isDeleted()) {
            log.error(String.format("User %s was deleted", user.getUsername()));
            throw new AuthException("User not found");
        }

        UserCredentials credentials = this.credentialRepository.findAllByUser(user).stream()
                .findFirst()
                .<AuthException>orElseThrow(() -> {
                    log.error(String.format("Credentials not found for user %s", user.getUsername()));
                    throw new AuthException("Authentication error. Please check your credentials");
                });
        return new UserProfile(user, credentials);
    }
}
