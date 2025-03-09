package com.app.heartfelt.controller;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.heartfelt.dto.TokenDTO;
import com.app.heartfelt.dto.UserLoginDTO;
import com.app.heartfelt.dto.UserRegistrationDTO;
import com.app.heartfelt.service.AuthService;
import com.app.heartfelt.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody UserRegistrationDTO registrationDto) {
        
        if(userService.existsByUsername(registrationDto.getUsername())) {
            return ResponseEntity.badRequest().body("Имя пользователя уже занято");
        }
        authService.registration(registrationDto);
        return ResponseEntity.ok("Регистрация прошла успешно");
    }
    
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> authenticate(@RequestBody UserLoginDTO loginDTO) {
        return ResponseEntity.ok(authService.authenticate(loginDTO));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<TokenDTO> refreshToken(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }
}
