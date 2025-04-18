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
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AnswerWithUsernameAndNickname {
    @Id
    @Column(name="a_id")
    private UUID id;

    @Column(name="a_q_id")
    private UUID questionId;

    @Column(name="a_u_id")
    private UUID psychologistId;

    @Column(name="a_text")
    private String text;

    @Column(name="a_created_at")
    private ZonedDateTime createdAt;

    @Column(name="u_nickname")
    private String nickname;

    @Column(name="u_username")
    private String username;
}
