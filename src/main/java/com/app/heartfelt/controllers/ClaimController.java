package com.app.heartfelt.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.heartfelt.dtos.ClaimDTO;
import com.app.heartfelt.services.ClaimService;

import jakarta.annotation.Nullable;



@RestController
@RequestMapping("/claims")
public class ClaimController {
    @Autowired
    private ClaimService claimService;

    @GetMapping
    public List<ClaimDTO> findAllClaims(@RequestParam @Nullable UUID senderId, @RequestParam @Nullable UUID userId) {
        List<ClaimDTO> findedClaims;
        if(senderId != null) {
            findedClaims = claimService.findClaimsBySenderId(senderId);
            System.out.println(findedClaims);
            return findedClaims;
        } else if(userId != null) {
            return claimService.findClaimsByUserId(userId);
        }
        findedClaims = claimService.findAllClaims();
        System.out.println(findedClaims);
        return findedClaims;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDTO> findClaimById(@PathVariable UUID id) {
        ClaimDTO claimDTO = claimService.findClaimById(id);
        return ResponseEntity.ok(claimDTO);
    }

    @PostMapping
    public ResponseEntity<ClaimDTO> complain(@RequestBody ClaimDTO claimDTO) {
        ClaimDTO newClaimDTO = claimService.saveClaim(claimDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newClaimDTO.getId())
            .toUri();
        try {
            return ResponseEntity.created(location).body(newClaimDTO);
        } catch(DataIntegrityViolationException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable UUID id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }
}
