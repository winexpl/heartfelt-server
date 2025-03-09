package com.app.heartfelt.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.dto.AnswerDTO;
import com.app.heartfelt.dto.QuestionDTO;
import com.app.heartfelt.model.Answer;
import com.app.heartfelt.model.Question;
import com.app.heartfelt.repository.JpaAnswerRepository;
import com.app.heartfelt.utils.MappingUtils;

@Service
public class AnswerService {
    @Autowired
    private JpaAnswerRepository answerRepository;
    @Autowired
    private MappingUtils mappingUtils;

    public List<AnswerDTO> findAllAnswersByQuestionId(UUID questionId) {
        return answerRepository.findAllByQuestionIdOrderByCreatedAtAsc(questionId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public AnswerDTO saveAnswer(AnswerDTO answerDTO) {
        return mappingUtils.convertToDTO(answerRepository.save(mappingUtils.convertToEntity(answerDTO)));
    }

    public void deleteAnswer(UUID id) {
        answerRepository.deleteById(id);
    }

    public AnswerDTO updateAnswer(UUID id, AnswerDTO answerDTO) {
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        Class<?> clazz = AnswerDTO.class;
        Field[] fields = clazz.getDeclaredFields();

        try {
            for(Field field : fields) {
                var value = field.get(answerDTO);
                if(value != null) field.set(answer, value);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_IMPLEMENTED);
        }
        return mappingUtils.convertToDTO(answerRepository.save(answer));
    }

    public List<AnswerDTO> findAnswerByPsychologistId(UUID userId) {
        return answerRepository.findByPsychologistId(userId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public AnswerDTO findAnswerById(UUID id) {
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return mappingUtils.convertToDTO(answer);
    }
}
