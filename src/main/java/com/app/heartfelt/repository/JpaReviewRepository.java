package com.app.heartfelt.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.heartfelt.model.Review;
import java.util.List;


public interface JpaReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findBySenderId(UUID senderId);
    List<Review> findByUserId(UUID userId);
}
