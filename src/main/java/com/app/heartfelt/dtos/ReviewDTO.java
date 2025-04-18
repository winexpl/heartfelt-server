package com.app.heartfelt.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private UUID id;
    private String senderUsername;
    private String receiverUsername;
    private UUID senderId;
    private UUID receiverId;
    private String text;
    private ZonedDateTime createdAt;
}
