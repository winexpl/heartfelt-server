package com.app.heartfelt.models.entities;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.app.heartfelt.models.RequestWithUsernameAndNickname;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests", schema = "public")
@NamedNativeQueries({
    @NamedNativeQuery(name = "Request.findAllWithSenderUsernameAndUserUsername",
    query = "SELECT r.r_id, r.r_u_id, r.r_text, r.r_created_at, u.u_username, u.u_nickname \n" +
        "FROM requests r LEFT JOIN users u ON r.r_u_id = u.u_id \n" +
        "ORDER BY r.r_created_at DESC",
    resultClass = RequestWithUsernameAndNickname.class)
})
public class Request {
    @Id
    @Column(name = "r_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "r_u_id")
    private UUID userId;

    @Column(name = "r_text")
    private String text;

    @Column(name = "r_created_at")
    private ZonedDateTime createdAt;
}