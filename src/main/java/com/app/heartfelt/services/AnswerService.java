package com.app.heartfelt.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.dtos.AnswerDTO;
import com.app.heartfelt.models.Notification;
import com.app.heartfelt.models.entities.Answer;
import com.app.heartfelt.models.entities.User;
import com.app.heartfelt.repositories.JpaAnswerRepository;
import com.app.heartfelt.security.Role;
import com.app.heartfelt.utils.MappingUtils;

@Service
public class AnswerService {
    @Autowired
    private JpaAnswerRepository answerRepository;
    @Autowired
    private MappingUtils mappingUtils;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<AnswerDTO> findAllAnswersByQuestionId(UUID questionId) {
        return answerRepository.findAllByQuestionIdOrderByCreatedAtAscWithUsernameAndNicknames(questionId).stream()
                .map(mappingUtils::convertToDTO).toList();
    }

    public AnswerDTO saveAnswer(AnswerDTO answerDTO) {
        User currentUser = userService.getCurrentUserDetails();
        answerDTO.setPsychologistId(currentUser.getId());
        answerDTO.setCreatedAt(ZonedDateTime.now());

        Notification notification = new Notification();
        notification.setMessage("На ваш вопрос был дан ответ!");
        notification.setQuestionId(answerDTO.getQuestionId());
        messagingTemplate.convertAndSend("/topic/notifications", notification);

        AnswerDTO savedAnswerDTO = mappingUtils
                .convertToDTO(answerRepository.save(mappingUtils.convertToEntity(answerDTO)));
        savedAnswerDTO.setUsername(currentUser.getUsername());
        return savedAnswerDTO;
    }

    public void deleteAnswer(UUID id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        User currentUser = userService.getCurrentUserDetails();
        if (currentUser.getId().equals(answer.getPsychologistId()) || currentUser.getAuthorities().contains(Role.ADMIN)) {
            answerRepository.deleteById(id);

        } else throw new SecurityException();
    }

    public AnswerDTO updateAnswer(UUID id, AnswerDTO answerDTO) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        User currentUser = userService.getCurrentUserDetails();
        if (currentUser.getId() != answer.getPsychologistId() || currentUser.getAuthorities().contains(Role.ADMIN))
            throw new SecurityException();
        answer.setText(answerDTO.getText());
        return mappingUtils.convertToDTO(answerRepository.save(answer));
    }

    public List<AnswerDTO> findAnswerByPsychologistId(UUID userId) {
        return answerRepository.findByPsychologistId(userId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public AnswerDTO findAnswerById(UUID id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return mappingUtils.convertToDTO(answer);
    }
}
