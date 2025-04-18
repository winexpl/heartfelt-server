package com.app.heartfelt.models.entities;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.app.heartfelt.models.ReviewWithSenderUsernameAndReceiverUsername;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name  =  "reviews",  schema  =  "public")
@NamedNativeQueries({
    @NamedNativeQuery(name = "Review.findAllWithSenderUsernameAndReceiverUsernameByReceiverId",
        query = "SELECT r.r_id, r.r_sender_id, r.r_receiver_id, r.r_text, r.r_created_at, u1.u_username as sender_username, u2.u_username as receiver_username \n" + //
        "FROM reviews r LEFT JOIN users u1 ON r.r_receiver_id=u1.u_id LEFT JOIN users u2 ON r.r_sender_id=u2.u_id \n" + //
        "WHERE r.r_receiver_id = :receiverId \n" +
        "ORDER BY r.r_created_at DESC",
        resultClass = ReviewWithSenderUsernameAndReceiverUsername.class
    )
})
public class Review {
    @Id
    @Column(name = "r_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "r_sender_id")
    private UUID senderId;

    @Column(name = "r_receiver_id")
    private UUID receiverId;

    @Column(name = "r_text")
    private String text;

    @Column(name = "r_created_at")
    private ZonedDateTime createdAt;
}