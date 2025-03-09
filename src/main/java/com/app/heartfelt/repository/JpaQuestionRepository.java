package com.app.heartfelt.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.heartfelt.model.Question;
import java.util.List;



public interface JpaQuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByUserId(UUID userId);
}