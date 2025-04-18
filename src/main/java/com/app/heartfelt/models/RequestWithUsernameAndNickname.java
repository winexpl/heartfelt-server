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
public class RequestWithUsernameAndNickname {
    @Id
    @Column(name = "r_id")
    private UUID id;

    @Column(name = "r_u_id")
    private UUID userId;

    @Column(name = "r_text")
    private String text;

    @Column(name = "u_username")
    private String username;

    @Column(name = "u_nickname")
    private String nickname;

    @Column(name = "r_created_at")
    private ZonedDateTime createdAt;
}
