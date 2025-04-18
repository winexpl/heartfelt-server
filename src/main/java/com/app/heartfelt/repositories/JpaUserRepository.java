package com.app.heartfelt.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.heartfelt.models.entities.User;


public interface JpaUserRepository extends JpaRepository<User, UUID> {
    
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query(nativeQuery = true)
    List<User> findAllByUsername(String username);
}