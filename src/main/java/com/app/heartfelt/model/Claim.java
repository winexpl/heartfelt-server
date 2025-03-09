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
@Table(name  =  "claims",  schema  =  "public")
public class Claim {
    @Id
    @Column(name = "c_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "c_sender_id", nullable = false)
    private UUID senderId;

    @Column(name = "c_user_id")
    private UUID userId;

    @Column(name = "c_text")
    private String text;

    @Column(name = "c_created_at")
    private ZonedDateTime createdAt;
}
