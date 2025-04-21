package com.app.heartfelt.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.dtos.QuestionDTO;
import com.app.heartfelt.dtos.UserDTO;
import com.app.heartfelt.models.entities.Question;
import com.app.heartfelt.models.entities.User;
import com.app.heartfelt.repositories.JpaQuestionRepository;
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
        UUID currentUserId = userService.getCurrentUserDetails().getId();
        return questionRepository.findAllWithSenderUsernameAndUserUsername(currentUserId).stream()
                .map(mappingUtils::convertToDTO).toList();
    }

    public List<QuestionDTO> findQuestionByUserId(UUID userId) {
        List<QuestionDTO> questionDTO = questionRepository
                .findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream().map(mappingUtils::convertToDTO).toList();
        UserDTO user = userService.findUserById(userId);
        User currentUser = userService.getCurrentUserDetails();
        if(!currentUser.getId().equals(user.getId())) {
            questionDTO = questionDTO.stream().filter(q -> !q.isAnonymous()).toList();
        }
        questionDTO.stream().map(dto -> {
            dto.setUsername(user.getUsername());
            dto.setNickname(user.getNickname());
            return dto;
        }).toList();
        return questionDTO;
    }

    public QuestionDTO findQuestionById(UUID id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        QuestionDTO questionDTO = mappingUtils.convertToDTO(question);
        UserDTO userDTO = userService.findUserById(question.getUserId());
        if (userDTO != null) {
            questionDTO.setUsername(userDTO.getUsername());
            questionDTO.setNickname(userDTO.getNickname());
        }
        return questionDTO;
    }

    public QuestionDTO ask(QuestionDTO questionDTO) {
        User currentUser = userService.getCurrentUserDetails();
        questionDTO.setCreatedAt(ZonedDateTime.now());
        questionDTO.setUserId(currentUser.getId());
        QuestionDTO savedQuestionDTO = mappingUtils
                .convertToDTO(questionRepository.save(mappingUtils.convertToEntity(questionDTO)));
        savedQuestionDTO.setUsername(currentUser.getUsername());
        return savedQuestionDTO;
    }

    public QuestionDTO updateQuestion(UUID id, QuestionDTO questionDTO) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        question.setText(questionDTO.getText());
        question.setTitle(questionDTO.getTitle());
        question.setAnonymous(questionDTO.isAnonymous());

        return mappingUtils.convertToDTO(questionRepository.save(question));
    }

    public List<QuestionDTO> findAllByTitle(String title) {
        return questionRepository.findAllByTitle(title).stream().map(mappingUtils::convertToDTO).toList();
    }

    public void deleteQuestion(UUID questionId) {
        questionRepository.deleteById(questionId);
    }
}