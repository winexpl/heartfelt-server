/**
 * Represents an Answer entity in the database.
 * This entity is mapped to the "answers" table in the "public" schema.
 * It stores information about answers provided by psychologists to questions.
 * 
 * <p>Annotations:</p>
 * <ul>
 *   <li>{@link Entity} - Specifies that this class is a JPA entity.</li>
 *   <li>{@link Table} - Maps this entity to the "answers" table in the "public" schema.</li>
 *   <li>{@link NamedNativeQueries} - Contains native SQL queries associated with this entity.</li>
 *   <li>{@link Data}, {@link Builder}, {@link NoArgsConstructor}, {@link AllArgsConstructor} - Lombok annotations for boilerplate code generation.</li>
 * </ul>
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code id} - The unique identifier for the answer. Auto-generated.</li>
 *   <li>{@code questionId} - The ID of the question this answer is associated with.</li>
 *   <li>{@code psychologistId} - The ID of the psychologist who provided the answer.</li>
 *   <li>{@code text} - The content of the answer.</li>
 *   <li>{@code createdAt} - The timestamp when the answer was created.</li>
 * </ul>
 * 
 * <p>Named Native Queries:</p>
 * <ul>
 *   <li>{@code Answer.findAllByQuestionIdOrderByCreatedAtAscWithUsernameAndNicknames} - 
 *       Retrieves all answers for a specific question, ordered by creation time in descending order,
 *       and includes the username and nickname of the psychologist.</li>
 * </ul>
 */
package com.app.heartfelt.models.entities;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.app.heartfelt.models.AnswerWithUsernameAndNickname;

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
@Table(name = "answers", schema = "public")
@NamedNativeQueries({
        @NamedNativeQuery(name = "Answer.findAllByQuestionIdOrderByCreatedAtAscWithUsernameAndNicknames", query = """
                SELECT a_id,a_u_id,a_q_id,a_text,a_created_at,u_nickname,u_username
                FROM answers a LEFT JOIN users u ON a.a_u_id=u.u_id WHERE a_q_id=:questionId
                ORDER BY a_created_at DESC;""", resultClass = AnswerWithUsernameAndNickname.class)
})
public class Answer {
    @Id
    @Column(name = "a_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "a_q_id", nullable = false)
    private UUID questionId;

    @Column(name = "a_u_id", nullable = false)
    private UUID psychologistId;

    @Column(name = "a_text", nullable = false)
    private String text;

    @Column(name = "a_created_at")
    private ZonedDateTime createdAt;
}
