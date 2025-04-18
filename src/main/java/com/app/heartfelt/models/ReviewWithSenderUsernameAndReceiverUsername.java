package com.app.heartfelt.models;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewWithSenderUsernameAndReceiverUsername {
    @Id
    @Column(name = "r_id", nullable = false)
    private UUID id;

    @Column(name = "receiver_username")
    private String senderUsername;

    @Column(name = "sender_username")
    private String receiverUsername;

    @Column(name = "r_sender_id")
    private UUID senderId;

    @Column(name = "r_receiver_id")
    private UUID receiverId;

    @Column(name = "r_text")
    private String text;

    @Column(name = "r_created_at")
    private ZonedDateTime createdAt;
}
