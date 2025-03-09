package com.app.heartfelt.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.dto.AnswerDTO;
import com.app.heartfelt.dto.ReviewDTO;
import com.app.heartfelt.model.Answer;
import com.app.heartfelt.model.Review;
import com.app.heartfelt.repository.JpaReviewRepository;
import com.app.heartfelt.utils.MappingUtils;

@Service
public class ReviewService {
    @Autowired
    private MappingUtils mappingUtils;
    @Autowired
    private JpaReviewRepository reviewRepository;

    public List<ReviewDTO> findAllReviews() {
        return reviewRepository.findAll().stream().map(mappingUtils::convertToDTO).toList();
    }

    public List<ReviewDTO> findReviewBySenderId(UUID senderId) {
        return reviewRepository.findBySenderId(senderId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public List<ReviewDTO> findReviewByUserId(UUID userId) {
        return reviewRepository.findByUserId(userId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public ReviewDTO findReviewById(UUID id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return mappingUtils.convertToDTO(review);
    }

    public ReviewDTO updateReview(UUID id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        Class<?> clazz = ReviewDTO.class;
        Field[] fields = clazz.getDeclaredFields();

        try {
            for(Field field : fields) {
                var value = field.get(reviewDTO);
                if(value != null) field.set(review, value);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_IMPLEMENTED);
        }
        return mappingUtils.convertToDTO(reviewRepository.save(review));
    }

    public ReviewDTO saveReview(ReviewDTO reviewDTO) {
        return mappingUtils.convertToDTO(reviewRepository.save(mappingUtils.convertToEntity(reviewDTO)));
    }

    public void deleteReview(UUID id) {
        reviewRepository.deleteById(id);
    }
}
