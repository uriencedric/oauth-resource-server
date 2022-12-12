package com.uriencedric.authorization.service.authProfiles;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uriencedric.authorization.persistence.entity.User;
import com.uriencedric.authorization.persistence.entity.UserCredentials;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserProfile implements UserDetails {

    private User user;
    private UserCredentials credentials;

    @Override
    public boolean isEnabled() {
        return this.user.isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getAuthorities();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO If credentials can expire, add check
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !this.user.isLocked();
    }

    @Override
    public String getPassword() {
        return this.credentials.getPasswordHash();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        // TODO If account can expire, add check
        return true;
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }
}
