package com.app.heartfelt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.heartfelt.dto.QuestionDTO;
import com.app.heartfelt.model.Question;
import com.app.heartfelt.repository.JpaQuestionRepository;

@Service
public class QuestionService {
    @Autowired
    private JpaQuestionRepository questionRepository;

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public QuestionDTO ask(QuestionDTO questionDTO) {
        return convertToDTO(questionRepository.save(convertToEntity(questionDTO)));
    }

    private QuestionDTO convertToDTO(Question question) {
        return QuestionDTO.builder()
            .profileId(question.getProfileId())
            .id(question.getId())
            .text(question.getText()).build();
    }

    private Question convertToEntity(QuestionDTO questionDTO) {
        return Question.builder()
            .profileId(questionDTO.getProfileId())
            .id(questionDTO.getId())
            .text(questionDTO.getText()).build();
    }
}