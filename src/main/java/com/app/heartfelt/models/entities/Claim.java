package com.app.heartfelt.models.entities;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.app.heartfelt.models.ClaimWithSenderUsernameAndUserUsername;
import com.app.heartfelt.models.enums.ClaimType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name  =  "claims",  schema  =  "public")
@NamedNativeQueries({
    @NamedNativeQuery(name = "Claim.findAllWithSenderUsernameAndReceiverUsername",
        query = "SELECT c_id,c_sender_id,c_receiver_id,c_text,c_created_at,c_q_id,c_a_id,c_type,u1.u_username AS receiver_username, u2.u_username as sender_username\n" + //
                        "FROM claims c LEFT JOIN users u1 ON c_receiver_id=u1.u_id\n" +
                        "LEFT JOIN users u2 ON c_sender_id=u2.u_id\n",
        resultClass = ClaimWithSenderUsernameAndUserUsername.class
    )
})
public class Claim {
    @Id
    @Column(name = "c_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "c_sender_id")
    private UUID senderId;

    @Column(name = "c_receiver_id")
    private UUID receiverId;

    @Column(name = "c_text")
    private String text;

    @Column(name = "c_created_at")
    private ZonedDateTime createdAt;

    @Column(name = "c_q_id")
    private UUID questionId;

    @Column(name = "c_a_id")
    private UUID answerId;

    @Column(name = "c_type")
    @Enumerated(EnumType.STRING)
    private ClaimType claimType;
}
