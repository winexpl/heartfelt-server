package com.app.heartfelt.models.entities;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.app.heartfelt.models.QuestionWithSenderUsernameAndUserUsername;

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
@Table(name  =  "questions",  schema  =  "public")
@NamedNativeQueries({
    @NamedNativeQuery(name = "Question.findAllWithSenderUsernameAndUserUsername",
    query = """
            SELECT q.q_id,q.q_u_id,q.q_text,q.q_created_at,q.q_title,q.q_is_anonymous, 
            CASE WHEN q.q_is_anonymous = false OR u.u_id = :currentUserId THEN u.u_username ELSE NULL END AS u_username, 
            CASE WHEN q.q_is_anonymous = false OR u.u_id = :currentUserId THEN u.u_nickname ELSE NULL END AS u_nickname 
            FROM questions q LEFT JOIN users u ON q.q_u_id = u.u_id 
            ORDER BY q.q_created_at DESC""",
    resultClass = QuestionWithSenderUsernameAndUserUsername.class),
    @NamedNativeQuery(name = "Question.findAllByTitle",
    query = "SELECT * FROM questions " +
        "WHERE q_title ILIKE '%' || :title || '%' ",
    resultClass = Question.class)
})
public class Question {
    @Id
    @Column(name = "q_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "q_u_id", nullable = false)
    private UUID userId;

    @Column(name = "q_title", nullable = false)
    private String title;

    @Column(name = "q_is_anonymous", nullable = false)
    private boolean anonymous;

    @Column(name = "q_text", nullable = false)
    private String text;

    @Column(name = "q_created_at")
    private ZonedDateTime createdAt;
}