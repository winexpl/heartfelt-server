package com.app.heartfelt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.heartfelt.repository.JpaAnswerRepository;

@Service
public class AnswerService {
    @Autowired
    private JpaAnswerRepository answerRepository;
}
