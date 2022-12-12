package com.uriencedric.authorization.persistence.repository;

import com.uriencedric.authorization.persistence.entity.ServiceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceUserRepository extends JpaRepository<ServiceUser, Long> {

    Optional<ServiceUser> findOneByName(String name);
    List<ServiceUser> findAllByIsServiceUser(Boolean isTechnicalUser);

}
