package com.uriencedric.authorization.service.authProfiles;

import com.uriencedric.authorization.common.exception.AuthException;
import com.uriencedric.authorization.persistence.entity.ServiceUser;
import com.uriencedric.authorization.persistence.entity.ServiceUserScope;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

@AllArgsConstructor
public class ClientProfile implements ClientDetails {

    private ServiceUser serviceUser;

    @Override
    public String getClientId() {
        return this.serviceUser.getName();
    }

    @Override
    public Set<String> getResourceIds() {
        return new HashSet<>();
    }

    @Override
    public boolean isSecretRequired() {
        return this.serviceUser.getIsSecretRequired();
    }

    @Override
    public String getClientSecret() {
        return this.serviceUser.getSecret();
    }

    @Override
    public boolean isScoped() {
        List<ServiceUserScope> scopes = this.serviceUser.getServiceUserScopes();
        return (scopes == null || scopes.isEmpty());
    }

    @Override
    public Set<String> getScope() {
        Set<String> scopeSet = new HashSet<>();
        this.serviceUser.getServiceUserScopes().forEach(clientScope -> scopeSet.add(clientScope.getScope().getName()));

        return scopeSet;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        Set<String> grantSet = new HashSet<>();
        this.serviceUser.getGrants().forEach(grant -> grantSet.add(grant.getName()));

        return grantSet;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        HashSet<String> uris = new HashSet<>();
        String uri = this.serviceUser.getRedirectURI();

        if (uri != null)
            uris.add(uri);

        return uris;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.serviceUser.getTokenValidity();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.serviceUser.getRefreshTokenValidity();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return this.serviceUser
                .getServiceUserScopes()
                .stream()
                .filter(clientScope -> clientScope.getScope().getName().equals(scope))
                .findFirst()
                .orElseThrow(() -> {
                    throw new AuthException("Invalid scopes.");
                })
                .isAutoApprove();
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return new HashMap<>();
    }
}
