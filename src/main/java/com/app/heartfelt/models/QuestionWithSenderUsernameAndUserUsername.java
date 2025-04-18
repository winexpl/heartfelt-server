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
public class QuestionWithSenderUsernameAndUserUsername {
    @Id
    @Column(name = "q_id")
    private UUID id;
    @Column(name = "u_username")
    private String username;
    @Column(name = "u_nickname")
    private String nickname;
    @Column(name = "q_u_id")
    private UUID userId;
    @Column(name = "q_text")
    private String text;
    @Column(name = "q_created_at")
    private ZonedDateTime createdAt;
    @Column(name = "q_title")
    private String title;
    @Column(name = "q_is_anonymous")
    private boolean anonymous;
}
