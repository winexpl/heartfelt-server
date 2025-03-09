package com.app.heartfelt.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.dto.ClaimDTO;
import com.app.heartfelt.dto.ReviewDTO;
import com.app.heartfelt.model.Answer;
import com.app.heartfelt.model.Claim;
import com.app.heartfelt.model.Review;
import com.app.heartfelt.repository.JpaClaimRepository;
import com.app.heartfelt.utils.MappingUtils;

@Service
public class ClaimService {
    @Autowired
    private JpaClaimRepository claimRepository;
    @Autowired
    private MappingUtils mappingUtils;

    public List<ClaimDTO> findAllClaims() {
        return claimRepository.findAll().stream().map(mappingUtils::convertToDTO).toList();
    }

    public ClaimDTO findClaimById(UUID id) {
        Claim claim = claimRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        return mappingUtils.convertToDTO(claim);
    }

    public List<ClaimDTO> findClaimsBySenderId(UUID senderId) {
        return claimRepository.findAllBySenderId(senderId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public List<ClaimDTO> findClaimsByUserId(UUID userId) {
        return claimRepository.findAllByUserId(userId).stream().map(mappingUtils::convertToDTO).toList();
    }

    public ClaimDTO updateReview(UUID id, ClaimDTO claimDTO) {
        Claim claim = claimRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        Class<?> clazz = ClaimDTO.class;
        Field[] fields = clazz.getDeclaredFields();

        try {
            for(Field field : fields) {
                var value = field.get(claimDTO);
                if(value != null) field.set(claim, value);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_IMPLEMENTED);
        }
        return mappingUtils.convertToDTO(claimRepository.save(claim));
    }

    public ClaimDTO saveClaim(ClaimDTO claimDTO) {
        return mappingUtils.convertToDTO(claimRepository.save(mappingUtils.convertToEntity(claimDTO)));
    }

    public void deleteClaim(UUID id) {
        claimRepository.deleteById(id);
    }
}
