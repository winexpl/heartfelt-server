package com.app.heartfelt.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.dtos.ReviewDTO;
import com.app.heartfelt.models.entities.Review;
import com.app.heartfelt.models.entities.User;
import com.app.heartfelt.repositories.JpaReviewRepository;
import com.app.heartfelt.repositories.JpaUserRepository;
import com.app.heartfelt.utils.MappingUtils;

@Service
public class ReviewService {
    @Autowired
    private UserService userService;
    @Autowired
    private MappingUtils mappingUtils;
    @Autowired
    private JpaReviewRepository reviewRepository;
    @Autowired
    private JpaUserRepository userRepository;

    ReviewService(UserService userService) {
        this.userService = userService;
    }

    public List<ReviewDTO> findAllReviews() {
        return reviewRepository.findAll().stream().map(mappingUtils::convertToDTO).toList();
    }

    public List<ReviewDTO> findReviewBySenderId(UUID senderId) {
        return reviewRepository.findBySenderId(senderId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public List<ReviewDTO> findReviewByReceiverId(UUID receiverId) {
        return reviewRepository.findAllWithSenderUsernameAndReceiverUsernameByReceiverId(receiverId).stream()
                .map(mappingUtils::convertToDTO).toList();
    }

    public ReviewDTO findReviewById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return mappingUtils.convertToDTO(review);
    }

    public ReviewDTO updateReview(UUID id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        review.setText(reviewDTO.getText());

        return mappingUtils.convertToDTO(reviewRepository.save(review));
    }

    public ReviewDTO saveReview(ReviewDTO reviewDTO) {
        User receiver;

        if (reviewDTO.getReceiverUsername() != null && reviewDTO.getReceiverId() == null) {
            receiver = userService.loadUserByUsername(reviewDTO.getReceiverUsername());
            reviewDTO.setReceiverId(receiver.getId());
        }
        reviewDTO.setCreatedAt(ZonedDateTime.now());

        User sender = userService.getCurrentUserDetails();
        reviewDTO.setSenderId(sender.getId());
        return mappingUtils.convertToDTO(reviewRepository.save(mappingUtils.convertToEntity(reviewDTO)));
    }

    public void deleteReview(UUID id) {
        reviewRepository.deleteById(id);
    }
}
