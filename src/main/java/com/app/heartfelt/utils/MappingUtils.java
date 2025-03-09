package com.app.heartfelt.utils;

import org.springframework.stereotype.Component;

import com.app.heartfelt.dto.AnswerDTO;
import com.app.heartfelt.dto.ClaimDTO;
import com.app.heartfelt.dto.QuestionDTO;
import com.app.heartfelt.dto.ReviewDTO;
import com.app.heartfelt.dto.UserDTO;
import com.app.heartfelt.dto.UserLoginDTO;
import com.app.heartfelt.dto.UserRegistrationDTO;
import com.app.heartfelt.model.Answer;
import com.app.heartfelt.model.Claim;
import com.app.heartfelt.model.Question;
import com.app.heartfelt.model.Review;
import com.app.heartfelt.model.User;

@Component
public class MappingUtils {
    
    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .nickname(user.getNickname())
            .createdAt(user.getCreatedAt())
            .role(user.getRole()).build();
    }

    public QuestionDTO convertToDTO(Question question) {
        return QuestionDTO.builder()
            .userId(question.getUserId())
            .id(question.getId())
            .text(question.getText())
            .createdAt(question.getCreatedAt()).build();
    }

    public AnswerDTO convertToDTO(Answer answer) {
        return AnswerDTO.builder()
            .id(answer.getId())
            .questionId(answer.getQuestionId())
            .psychologistId(answer.getPsychologistId())
            .text(answer.getText())
            .createdAt(answer.getCreatedAt()).build();
    }

    public ClaimDTO convertToDTO(Claim claim) {
        return ClaimDTO.builder()
            .id(claim.getId())
            .senderId(claim.getSenderId())
            .userId(claim.getUserId())
            .text(claim.getText())
            .createdAt(claim.getCreatedAt()).build();
    }

    public ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
            .id(review.getId())
            .senderId(review.getSenderId())
            .userId(review.getUserId())
            .text(review.getText())
            .createdAt(review.getCreatedAt()).build();
    }

    public User convertToEntity(UserDTO userDTO) {
        return User.builder()
            .id(userDTO.getId())
            .nickname(userDTO.getNickname())
            .role(userDTO.getRole())
            .username(userDTO.getUsername())
            .createdAt(userDTO.getCreatedAt()).build();
    }

    public User convertToEntity(UserLoginDTO userLoginDTO) {
        return User.builder()
            .username(userLoginDTO.getUsername())
            .password(userLoginDTO.getPassword()).build();
    }

    public User convertToEntity(UserRegistrationDTO userRegistrationDTO) {
        return User.builder()
            .username(userRegistrationDTO.getUsername())
            .nickname(userRegistrationDTO.getNickname())
            .password(userRegistrationDTO.getPassword())
            .role(userRegistrationDTO.getRole()).build();
    }

    public Claim convertToEntity(ClaimDTO claimDTO) {
        return Claim.builder()
            .id(claimDTO.getId())
            .senderId(claimDTO.getSenderId())
            .userId(claimDTO.getUserId())
            .text(claimDTO.getText())
            .createdAt(claimDTO.getCreatedAt()).build();
    }

    public Answer convertToEntity(AnswerDTO answerDTO) {
        return Answer.builder()
            .id(answerDTO.getId())
            .questionId(answerDTO.getQuestionId())
            .psychologistId(answerDTO.getPsychologistId())
            .text(answerDTO.getText())
            .createdAt(answerDTO.getCreatedAt()).build();
    }

    public Question convertToEntity(QuestionDTO questionDTO) {
        return Question.builder()
            .id(questionDTO.getId())
            .userId(questionDTO.getUserId())
            .text(questionDTO.getText())
            .createdAt(questionDTO.getCreatedAt()).build();
    }

    public Review convertToEntity(ReviewDTO reviewDTO) {
        return Review.builder()
            .id(reviewDTO.getId())
            .senderId(reviewDTO.getSenderId())
            .userId(reviewDTO.getUserId())
            .text(reviewDTO.getText())
            .createdAt(reviewDTO.getCreatedAt()).build();
    }
}
