package com.app.heartfelt.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
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
    private UUID questionId;
    private UUID psychologistId;
    private String text;
    private ZonedDateTime createdAt;
}