package com.app.heartfelt.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.heartfelt.models.QuestionWithSenderUsernameAndUserUsername;
import com.app.heartfelt.models.entities.Question;

public interface JpaQuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByUserId(UUID userId, Sort sort);

    @Query(nativeQuery = true)
    List<Question> findAllByTitle(String title);

    @Query(nativeQuery = true)
    List<QuestionWithSenderUsernameAndUserUsername> findAllWithSenderUsernameAndUserUsername(UUID currentUserId);
}