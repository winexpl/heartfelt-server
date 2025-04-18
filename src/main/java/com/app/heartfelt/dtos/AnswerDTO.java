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
public class AnswerDTO {
    private UUID id;
    private String username;
    private String nickname;
    private UUID questionId;
    private UUID psychologistId;
    private String text;
    private ZonedDateTime createdAt;
}