package com.uriencedric.authorization.persistence.repository;

import com.uriencedric.authorization.persistence.entity.User;
import com.uriencedric.authorization.persistence.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CredentialRepository extends JpaRepository<UserCredentials, Long> {

    List<UserCredentials> findAllByUser(User user);
}
