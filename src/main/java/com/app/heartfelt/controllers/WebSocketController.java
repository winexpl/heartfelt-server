package com.app.heartfelt.controllers;

import javax.management.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.app.heartfelt.dtos.AnswerDTO;
import com.app.heartfelt.dtos.QuestionDTO;
import com.app.heartfelt.dtos.UserDTO;
import com.app.heartfelt.models.entities.User;
import com.app.heartfelt.services.AuthService;
import com.app.heartfelt.services.QuestionService;
import com.app.heartfelt.services.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebSocketController {
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private AuthService authService;

    @MessageMapping("/send-question") // Конечная точка для обработки сообщений
    @SendTo("/topic/questions") // Отправка ответа подписчикам
    public QuestionDTO handleQuestion(QuestionDTO questionDTO) {
        if(!questionDTO.isAnonymous()) {
            UserDTO userDTO = userService.findUserById(questionDTO.getUserId());
            questionDTO.setNickname(userDTO.getNickname());
            questionDTO.setUsername(userDTO.getUsername());
        } else {
            questionDTO.setUserId(null);
        }
        return questionDTO;
    }


    @MessageMapping("/send-answer")
    @JsonInclude
    public void handleAnswer(AnswerDTO answerDTO) {
        QuestionDTO questionDTO = questionService.findQuestionById(answerDTO.getQuestionId());
        UserDTO userDTO = userService.findUserById(questionDTO.getUserId());
        UserDTO psychologistDTO = userService.findUserById(answerDTO.getPsychologistId());

        User currentUser = userService.getCurrentUserDetails();
        System.out.println(currentUser);
        answerDTO.setNickname(psychologistDTO.getNickname());
        answerDTO.setUsername(psychologistDTO.getUsername());

        messagingTemplate.convertAndSend("/topic/" + userDTO.getUsername() + "/questions/answer", answerDTO);
        messagingTemplate.convertAndSend("/topic/" + answerDTO.getQuestionId() + "/answers", answerDTO);
    }
}
