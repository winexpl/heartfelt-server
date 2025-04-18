package com.app.heartfelt.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.heartfelt.dtos.QuestionDTO;
import com.app.heartfelt.dtos.RequestDTO;
import com.app.heartfelt.services.RequestService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/requests")
public class RequsetController {
    @Autowired
    private RequestService requestService;

    @GetMapping
    public List<RequestDTO> findAll() {
        return requestService.findAll();
    }

    @PostMapping
    public ResponseEntity<RequestDTO> saveRequest(@RequestBody RequestDTO requestDTO) {
        RequestDTO newRequestDTO = requestService.saveRequest(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newRequestDTO.getId())
            .toUri();
        return ResponseEntity.created(location).body(newRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable UUID id) {
        requestService.deleteRequest(id);
        return ResponseEntity.ok().build();
    }
}
