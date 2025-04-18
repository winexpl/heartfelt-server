package com.app.heartfelt.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.heartfelt.models.ReviewWithSenderUsernameAndReceiverUsername;
import com.app.heartfelt.models.entities.Review;

import java.util.List;


public interface JpaReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findBySenderId(UUID senderId);

    List<Review> findByReceiverId(UUID userId);

    @Query(nativeQuery = true)
    List<ReviewWithSenderUsernameAndReceiverUsername> findAllWithSenderUsernameAndReceiverUsernameByReceiverId(UUID receiverId);
}
