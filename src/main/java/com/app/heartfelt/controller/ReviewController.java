package com.app.heartfelt.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.heartfelt.dto.AnswerDTO;
import com.app.heartfelt.dto.ClaimDTO;
import com.app.heartfelt.dto.ReviewDTO;
import com.app.heartfelt.service.ClaimService;
import com.app.heartfelt.service.ReviewService;
import com.app.heartfelt.utils.MappingUtils;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<ReviewDTO> findAllReviews() {
        return reviewService.findAllReviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> findReviewById(@PathVariable UUID id) {
        ReviewDTO reviewDTO = reviewService.findReviewById(id);
        return ResponseEntity.ok(reviewDTO);
    }
    
    @GetMapping("/{senderId}")
    public List<ReviewDTO> findReviewBySenderId(@PathVariable UUID senderId) {
        return reviewService.findReviewBySenderId(senderId);
    }
    
    @GetMapping("/{userId}")
    public List<ReviewDTO> findReviewByUserId(@PathVariable UUID userId) {
        return reviewService.findReviewByUserId(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable UUID id, @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ofNullable(reviewService.updateReview(id, reviewDTO));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> saveReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO newReviewDTO = reviewService.saveReview(reviewDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newReviewDTO.getId())
            .toUri();
        return ResponseEntity.created(location).body(newReviewDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
