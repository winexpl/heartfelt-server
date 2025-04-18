package com.app.heartfelt.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.heartfelt.models.ClaimWithSenderUsernameAndUserUsername;
import com.app.heartfelt.models.entities.Claim;

public interface JpaClaimRepository extends JpaRepository<Claim, UUID> {
    List<Claim> findAllBySenderId(UUID senderId);
    List<Claim> findAllByReceiverId(UUID userId);

    @Query(nativeQuery = true, name = "Claim.findAllWithSenderUsernameAndReceiverUsername")
    List<ClaimWithSenderUsernameAndUserUsername> findAllWithSenderUsernameAndReceiverUsername();
}
