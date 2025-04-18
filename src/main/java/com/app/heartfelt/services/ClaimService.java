package com.app.heartfelt.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.dtos.ClaimDTO;
import com.app.heartfelt.models.entities.Answer;
import com.app.heartfelt.models.entities.Claim;
import com.app.heartfelt.models.entities.Question;
import com.app.heartfelt.models.entities.User;
import com.app.heartfelt.repositories.JpaAnswerRepository;
import com.app.heartfelt.repositories.JpaClaimRepository;
import com.app.heartfelt.repositories.JpaQuestionRepository;
import com.app.heartfelt.utils.MappingUtils;

@Service
public class ClaimService {
    @Autowired
    private JpaClaimRepository claimRepository;
    @Autowired
    private MappingUtils mappingUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private JpaAnswerRepository answerRepository;
    @Autowired
    private JpaQuestionRepository questionRepository;

    public List<ClaimDTO> findAllClaims() {
        return claimRepository.findAllWithSenderUsernameAndReceiverUsername().stream().map(mappingUtils::convertToDTO)
                .toList();
    }

    public ClaimDTO findClaimById(UUID id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return mappingUtils.convertToDTO(claim);
    }

    public List<ClaimDTO> findClaimsBySenderId(UUID senderId) {
        return claimRepository.findAllBySenderId(senderId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public List<ClaimDTO> findClaimsByUserId(UUID userId) {
        return claimRepository.findAllByReceiverId(userId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public ClaimDTO saveClaim(ClaimDTO claimDTO) {
        User currentUser = userService.getCurrentUserDetails();
        claimDTO.setSenderId(currentUser.getId());
        claimDTO.setCreatedAt(ZonedDateTime.now());
        switch (claimDTO.getClaimType()) {
            case ANSWER -> {
                Answer answer = answerRepository.findById(claimDTO.getAnswerId())
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
                claimDTO.setReceiverId(answer.getPsychologistId());
            }
            case QUESTION -> {
                Question question = questionRepository.findById(claimDTO.getQuestionId())
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
                claimDTO.setReceiverId(question.getUserId());
            }
            default -> throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return mappingUtils.convertToDTO(claimRepository.save(mappingUtils.convertToEntity(claimDTO)));
    }

    public void deleteClaim(UUID id) {
        claimRepository.deleteById(id);
    }
}
