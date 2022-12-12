package com.uriencedric.authorization.persistence.repository;

import com.uriencedric.authorization.persistence.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    Optional<Scope> findOneByName(String name);
}
