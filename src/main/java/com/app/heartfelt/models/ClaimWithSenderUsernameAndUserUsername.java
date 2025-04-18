package com.app.heartfelt.models;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.app.heartfelt.models.enums.ClaimType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class ClaimWithSenderUsernameAndUserUsername {
    @Id
    @Column(name="c_id")
    private UUID id;
    
    @Column(name="c_sender_id")
    private UUID senderId;

    @Column(name="c_receiver_id")
    private UUID receiverId;

    @Column(name="sender_username")
    private String senderUsername;

    @Column(name="receiver_username")
    private String receiverUsername;

    @Column(name="c_text")
    private String text;

    @Column(name="c_created_at")
    private ZonedDateTime createdAt;

    @Column(name="c_a_id")
    private UUID answerId;

    @Column(name="c_q_id")
    private UUID questionId;

    @Column(name="c_type")
    @Enumerated(EnumType.STRING)
    private ClaimType claimType;
}
