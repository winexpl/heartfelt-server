package com.app.heartfelt.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.dto.QuestionDTO;
import com.app.heartfelt.model.Question;
import com.app.heartfelt.repository.JpaQuestionRepository;
import com.app.heartfelt.utils.MappingUtils;

@Service
public class QuestionService {
    @Autowired
    private JpaQuestionRepository questionRepository;
    @Autowired
    private MappingUtils mappingUtils;
    @Autowired
    private UserService userService;

    public List<QuestionDTO> findAllQuestions() {
        return questionRepository.findAll().stream().map(mappingUtils::convertToDTO).toList();
    }

    public List<QuestionDTO> findQuestionByUserId(UUID userId) {
        return questionRepository.findByUserId(userId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public QuestionDTO findQuestionById(UUID id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return mappingUtils.convertToDTO(question);
    }

    public QuestionDTO ask(QuestionDTO questionDTO) {
        System.out.println(userService.getCurrentUserDetails().getId());
        questionDTO.setUserId(userService.getCurrentUserDetails().getId());
        return mappingUtils.convertToDTO(questionRepository.save(mappingUtils.convertToEntity(questionDTO)));
    }

    public QuestionDTO updateQuestion(UUID id, QuestionDTO questionDTO) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        Class<?> clazz = QuestionDTO.class;
        Field[] fields = clazz.getDeclaredFields();

        try {
            for(Field field : fields) {
                var value = field.get(questionDTO);
                if(value != null) field.set(question, value);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_IMPLEMENTED);
        }
        return mappingUtils.convertToDTO(questionRepository.save(question));
    }
}