package com.app.heartfelt.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.heartfelt.models.AnswerWithUsernameAndNickname;
import com.app.heartfelt.models.entities.Answer;

public interface JpaAnswerRepository extends JpaRepository<Answer, UUID>{
    @Query(nativeQuery = true, name = "Answer.findAllByQuestionIdOrderByCreatedAtAscWithUsernameAndNicknames")
    public List<AnswerWithUsernameAndNickname> findAllByQuestionIdOrderByCreatedAtAscWithUsernameAndNicknames(UUID questionId);
    
    public List<Answer> findByPsychologistId(UUID userId);
}
