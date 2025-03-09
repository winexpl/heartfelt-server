package com.app.heartfelt.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name  =  "reviews",  schema  =  "public")
public class Review {
    @Id
    @Column(name = "r_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "r_sender_id", nullable = false)
    private UUID senderId;

    @Column(name = "r_user_id")
    private UUID userId;

    @Column(name = "r_text")
    private String text;

    @Column(name = "r_created_at")
    private ZonedDateTime createdAt;
}