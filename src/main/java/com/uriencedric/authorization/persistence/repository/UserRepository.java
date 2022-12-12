package com.uriencedric.authorization.persistence.repository;


import com.uriencedric.authorization.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByUsername(String username);
}

