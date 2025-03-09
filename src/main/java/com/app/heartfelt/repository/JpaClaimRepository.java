package com.app.heartfelt.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.heartfelt.model.Claim;

public interface JpaClaimRepository extends JpaRepository<Claim, UUID> {
    List<Claim> findAllBySenderId(UUID senderId);
    List<Claim> findAllByUserId(UUID userId);

}
