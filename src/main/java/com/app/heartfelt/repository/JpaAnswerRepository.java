package com.app.heartfelt.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.heartfelt.model.Answer;

public interface JpaAnswerRepository extends JpaRepository<Answer, UUID>{
    
}
