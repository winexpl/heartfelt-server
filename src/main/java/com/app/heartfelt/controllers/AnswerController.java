package com.app.heartfelt.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.heartfelt.dtos.AnswerDTO;
import com.app.heartfelt.services.AnswerService;

import io.micrometer.common.lang.Nullable;


@RestController
@RequestMapping("/answers")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @GetMapping
    public List<AnswerDTO> findAllAnswersByQuestionId(@RequestParam @Nullable UUID questionId, @RequestParam @Nullable UUID psychologistId) {
        if(questionId != null) return answerService.findAllAnswersByQuestionId(questionId);
        else if(psychologistId != null) return answerService.findAnswerByPsychologistId(psychologistId);
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
    }
    
    @PostMapping
    public ResponseEntity<AnswerDTO> reply(@RequestBody AnswerDTO answerDTO) {
        AnswerDTO newAnswerDTO = answerService.saveAnswer(answerDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newAnswerDTO.getId())
            .toUri();
        return ResponseEntity.created(location).body(newAnswerDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable UUID id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable UUID id, @RequestBody AnswerDTO answerDTO) {
        return ResponseEntity.ofNullable(answerService.updateAnswer(id, answerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerDTO> findAnswerById(@PathVariable UUID id) {
        return ResponseEntity.ok(answerService.findAnswerById(id));
    }
}
