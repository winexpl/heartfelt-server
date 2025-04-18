package com.app.heartfelt.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.app.heartfelt.models.enums.ClaimType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDTO {
    private UUID id;
    private UUID senderId;
    private UUID receiverId;
    private String senderUsername;
    private String receiverUsername;
    private String text;
    private ZonedDateTime createdAt;
    private UUID answerId;
    private UUID questionId;
    private ClaimType claimType;
}
