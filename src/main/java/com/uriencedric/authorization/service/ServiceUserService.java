package com.uriencedric.authorization.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import com.uriencedric.authorization.common.ServiceUserDto;
import com.uriencedric.authorization.persistence.entity.Grant;
import com.uriencedric.authorization.persistence.entity.ServiceUser;
import com.uriencedric.authorization.persistence.entity.ServiceUserScope;
import com.uriencedric.authorization.persistence.repository.GrantTypeRepository;
import com.uriencedric.authorization.persistence.repository.ScopeRepository;
import com.uriencedric.authorization.persistence.repository.ServiceUserRepository;
import com.uriencedric.authorization.persistence.repository.ServiceUserScopeRepository;
import com.uriencedric.authorization.service.authProfiles.ClientProfile;

@Service
public class ServiceUserService implements ClientDetailsService {

    private static final String[] GRANTS = { "client_credentials" };
    private static final String[] SCOPES = { "service_user" };

    @Autowired
    protected ServiceUserRepository serviceUserRepository;
    @Autowired
    protected GrantTypeRepository grantTypeRepository;
    @Autowired
    protected ScopeRepository scopeRepository;
    @Autowired
    protected ServiceUserScopeRepository serviceUserScopeRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        ServiceUser serviceUser = this.serviceUserRepository.findOneByName(clientId)
                .orElseThrow(() -> new RuntimeException("Client does not exists"));

        return new ClientProfile(serviceUser);
    }

    public List<ServiceUserDto> getAllServiceUsers() {
        return this.serviceUserRepository.findAllByIsServiceUser(true).stream().map(ServiceUser::toDto)
                .collect(Collectors.toList());
    }

    public ServiceUserDto addServiceUser(ServiceUserDto.Create dto) {
        this.serviceUserRepository.findOneByName(dto.getName())
                .ifPresent(c -> {
                    throw new RuntimeException("User is already created");
                });

        Set<Grant> grants = new HashSet<>();
        Arrays.stream(GRANTS).forEach(grantType -> grants.add(grantTypeRepository.findOneByName(grantType)
                .orElseThrow(() -> new RuntimeException("Cannot not retrieve GRANT"))));

        ServiceUser serviceUser = ServiceUser.builder()
                .name(dto.getName())
                .secret(dto.getSecret())
                .isSecretRequired(true)
                .isTechnicalUser(true)
                .refreshTokenValidity(dto.getRefreshTokenValidity())
                .tokenValidity(dto.getTokenValidity())
                .grants(grants)
                .build();

        serviceUserRepository.save(serviceUser);

        Arrays.stream(SCOPES).forEach(scope -> {
            serviceUserScopeRepository.save(ServiceUserScope.builder()
                    .serviceUser(serviceUser)
                    .scope(scopeRepository.findOneByName(scope)
                            .orElseThrow(() -> new RuntimeException("Cannot find provided scopes.")))
                    .build());
        });

        return serviceUser.toDto();
    }

    public ServiceUserDto updateServiceUser(String name, ServiceUserDto.Update dto) {
        ServiceUser serviceUser = this.serviceUserRepository.findOneByName(name)
                .orElseThrow(() -> new RuntimeException("Service user does not exists"));
        serviceUser.setRefreshTokenValidity(dto.getRefreshTokenValidity());
        serviceUser.setTokenValidity(dto.getTokenValidity());
        serviceUserRepository.save(serviceUser);
        return serviceUser.toDto();
    }

    public ServiceUserDto updateServiceUserSecret(String name, ServiceUserDto.UpdateSecret dto) {
        ServiceUser serviceUser = this.serviceUserRepository.findOneByName(name)
                .orElseThrow(() -> new RuntimeException("Service user does not exists"));
        serviceUser.setSecret(dto.getSecret());
        serviceUserRepository.save(serviceUser);
        return serviceUser.toDto();
    }

    public ServiceUserDto deleteServiceUser(String name) {
        ServiceUser serviceUser = this.serviceUserRepository.findOneByName(name)
                .orElseThrow(() -> new RuntimeException("Service user does not exists"));
        serviceUserScopeRepository.delete(serviceUser.getServiceUserScopes());
        serviceUserRepository.delete(serviceUser);
        return serviceUser.toDto();
    }

}
