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
import com.app.heartfelt.utils.MappingUtils;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/claims")
public class ClaimController {
    @Autowired
    private ClaimService claimService;

    @GetMapping
    public List<ClaimDTO> findAllClaims() {
        return claimService.findAllClaims();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDTO> findClaimById(@PathVariable UUID id) {
        ClaimDTO claimDTO = claimService.findClaimById(id);
        return ResponseEntity.ok(claimDTO);
    }
    
    @GetMapping("/{senderId}")
    public List<ClaimDTO> findClaimsBySenderId(@PathVariable UUID senderId) {
        return claimService.findClaimsBySenderId(senderId);
    }
    
    @GetMapping("/{userId}")
    public List<ClaimDTO> findClaimsByUserId(@PathVariable UUID userId) {
        return claimService.findClaimsByUserId(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaimDTO> updateClaim(@PathVariable UUID id, @RequestBody ClaimDTO claimDTO) {
        return ResponseEntity.ofNullable(claimService.updateReview(id, claimDTO));
    }

    @PostMapping
    public ResponseEntity<ClaimDTO> complain(@RequestBody ClaimDTO claimDTO) {
        ClaimDTO newClaimDTO = claimService.saveClaim(claimDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newClaimDTO.getId())
            .toUri();
        return ResponseEntity.created(location).body(newClaimDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable UUID id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }
}
