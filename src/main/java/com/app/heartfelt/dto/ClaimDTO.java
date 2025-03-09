package com.app.heartfelt.dto;

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
public class ClaimDTO {
    private UUID id;
    private UUID senderId;
    private UUID userId;
    private String text;
    private ZonedDateTime createdAt;
}
