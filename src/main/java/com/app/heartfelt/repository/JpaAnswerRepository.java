package com.app.heartfelt.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.heartfelt.model.Answer;

public interface JpaAnswerRepository extends JpaRepository<Answer, UUID>{
    public List<Answer> findAllByQuestionIdOrderByCreatedAtAsc(UUID questionId);
    public List<Answer> findByPsychologistId(UUID userId);
}
