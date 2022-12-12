package com.uriencedric.authorization.persistence.repository;

import com.uriencedric.authorization.persistence.entity.Grant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrantTypeRepository extends JpaRepository<Grant, Long> {
    Optional<Grant> findOneByName(String name);
}
