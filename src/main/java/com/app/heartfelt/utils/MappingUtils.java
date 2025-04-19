package com.app.heartfelt.utils;

import org.springframework.stereotype.Component;

import com.app.heartfelt.dtos.AnswerDTO;
import com.app.heartfelt.dtos.ClaimDTO;
import com.app.heartfelt.dtos.QuestionDTO;
import com.app.heartfelt.dtos.RequestDTO;
import com.app.heartfelt.dtos.ReviewDTO;
import com.app.heartfelt.dtos.UserDTO;
import com.app.heartfelt.dtos.UserLoginDTO;
import com.app.heartfelt.dtos.UserRegistrationDTO;
import com.app.heartfelt.models.AnswerWithUsernameAndNickname;
import com.app.heartfelt.models.ClaimWithSenderUsernameAndUserUsername;
import com.app.heartfelt.models.QuestionWithSenderUsernameAndUserUsername;
import com.app.heartfelt.models.RequestWithUsernameAndNickname;
import com.app.heartfelt.models.ReviewWithSenderUsernameAndReceiverUsername;
import com.app.heartfelt.models.entities.Answer;
import com.app.heartfelt.models.entities.Claim;
import com.app.heartfelt.models.entities.Question;
import com.app.heartfelt.models.entities.Request;
import com.app.heartfelt.models.entities.Review;
import com.app.heartfelt.models.entities.User;

@Component
public class MappingUtils {

    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .about(user.getAbout())
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
                .anonymous(question.isAnonymous())
                .title(question.getTitle())
                .createdAt(question.getCreatedAt()).build();
    }

    public QuestionDTO convertToDTO(QuestionWithSenderUsernameAndUserUsername question) {
        return QuestionDTO.builder()
                .userId(question.getUserId())
                .id(question.getId())
                .text(question.getText())
                .anonymous(question.isAnonymous())
                .title(question.getTitle())
                .createdAt(question.getCreatedAt())
                .username(question.getUsername())
                .nickname(question.getNickname()).build();
    }

    public AnswerDTO convertToDTO(Answer answer) {
        return AnswerDTO.builder()
                .id(answer.getId())
                .questionId(answer.getQuestionId())
                .psychologistId(answer.getPsychologistId())
                .text(answer.getText())
                .createdAt(answer.getCreatedAt()).build();
    }

    public AnswerDTO convertToDTO(AnswerWithUsernameAndNickname answer) {
        return AnswerDTO.builder()
                .id(answer.getId())
                .questionId(answer.getQuestionId())
                .psychologistId(answer.getPsychologistId())
                .text(answer.getText())
                .nickname(answer.getNickname())
                .username(answer.getUsername())
                .createdAt(answer.getCreatedAt()).build();
    }

    public ClaimDTO convertToDTO(Claim claim) {
        return ClaimDTO.builder()
                .id(claim.getId())
                .senderId(claim.getSenderId())
                .receiverId(claim.getReceiverId())
                .claimType(claim.getClaimType())
                .answerId(claim.getAnswerId())
                .questionId(claim.getQuestionId())
                .text(claim.getText())
                .createdAt(claim.getCreatedAt()).build();
    }

    public ClaimDTO convertToDTO(ClaimWithSenderUsernameAndUserUsername claim) {
        return ClaimDTO.builder()
                .id(claim.getId())
                .senderId(claim.getSenderId())
                .receiverId(claim.getReceiverId())
                .claimType(claim.getClaimType())
                .answerId(claim.getAnswerId())
                .questionId(claim.getQuestionId())
                .text(claim.getText())
                .senderUsername(claim.getSenderUsername())
                .receiverUsername(claim.getReceiverUsername())
                .createdAt(claim.getCreatedAt()).build();
    }

    public ReviewDTO convertToDTO(ReviewWithSenderUsernameAndReceiverUsername review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .senderId(review.getSenderId())
                .receiverId(review.getReceiverId())
                .text(review.getText())
                .createdAt(review.getCreatedAt())
                .senderUsername(review.getSenderUsername())
                .receiverUsername(review.getReceiverUsername())
                .build();
    }

    public ReviewDTO convertToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .senderId(review.getSenderId())
                .receiverId(review.getReceiverId())
                .text(review.getText())
                .createdAt(review.getCreatedAt()).build();
    }

    public User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .about(userDTO.getAbout())
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
                .receiverId(claimDTO.getReceiverId())
                .questionId(claimDTO.getQuestionId())
                .answerId(claimDTO.getAnswerId())
                .text(claimDTO.getText())
                .claimType(claimDTO.getClaimType())
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
                .createdAt(questionDTO.getCreatedAt())
                .anonymous(questionDTO.isAnonymous())
                .title(questionDTO.getTitle())
                .build();
    }

    public Review convertToEntity(ReviewDTO reviewDTO) {
        return Review.builder()
                .id(reviewDTO.getId())
                .senderId(reviewDTO.getSenderId())
                .receiverId(reviewDTO.getReceiverId())
                .text(reviewDTO.getText())
                .createdAt(reviewDTO.getCreatedAt()).build();
    }

    public Request convertToEntity(RequestDTO requestDTO) {
        return Request.builder()
                .id(requestDTO.getId())
                .userId(requestDTO.getUserId())
                .text(requestDTO.getText())
                .createdAt(requestDTO.getCreatedAt()).build();
    }

    public RequestDTO convertToDTO(Request request) {
        return RequestDTO.builder()
                .id(request.getId())
                .userId(request.getUserId())
                .text(request.getText())
                .createdAt(request.getCreatedAt()).build();
    }

    public RequestDTO convertToDTO(RequestWithUsernameAndNickname request) {
        return RequestDTO.builder()
                .id(request.getId())
                .userId(request.getUserId())
                .text(request.getText())
                .username(request.getUsername())
                .nickname(request.getNickname())
                .createdAt(request.getCreatedAt()).build();
    }
}
