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
public class RequestDTO {
    private UUID id;
    private UUID userId;
    private String text;
    private String username;
    private String nickname;
    private ZonedDateTime createdAt;
}
