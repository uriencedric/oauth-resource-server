package com.uriencedric.authorization.persistence.repository;

import com.uriencedric.authorization.persistence.entity.ServiceUserScope;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceUserScopeRepository extends JpaRepository<ServiceUserScope, Long> {
}
