package com.app.heartfelt.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.heartfelt.dtos.RequestDTO;
import com.app.heartfelt.models.entities.User;
import com.app.heartfelt.repositories.JpaRequestRepository;
import com.app.heartfelt.utils.MappingUtils;

@Service
public class RequestService {
    @Autowired
    private JpaRequestRepository requestRepository;
    @Autowired
    private MappingUtils mappingUtils;
    @Autowired
    private UserService userService;

    public List<RequestDTO> findAll() {
        return requestRepository.findAllWithSenderUsernameAndUserUsername().stream().map(mappingUtils::convertToDTO)
                .toList();
    }

    public RequestDTO saveRequest(RequestDTO requestDTO) {
        User currentUser = userService.getCurrentUserDetails();
        requestDTO.setUserId(currentUser.getId());
        return mappingUtils.convertToDTO(requestRepository.save(mappingUtils.convertToEntity(requestDTO)));
    }

    public void deleteRequest(UUID id) {
        requestRepository.deleteById(id);
    }

}
