package com.app.heartfelt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.heartfelt.dto.AccessTokenDTO;
import com.app.heartfelt.dto.TokenDTO;
import com.app.heartfelt.dto.UserLoginDTO;
import com.app.heartfelt.dto.UserRegistrationDTO;
import com.app.heartfelt.service.AuthService;
import com.app.heartfelt.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CookieValue;
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
    public ResponseEntity<AccessTokenDTO> authenticate(@RequestBody UserLoginDTO loginDTO, HttpServletResponse response) {
        TokenDTO tokenDTO = authService.authenticate(loginDTO);
        Cookie refreshCookie = new Cookie("refreshToken", tokenDTO.getRefreshToken());
        refreshCookie.setHttpOnly(true); // Запрет доступа из JavaScript
        refreshCookie.setSecure(false); // Только если вы используете HTTP
        refreshCookie.setPath("/refresh_token");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // Срок жизни - 7 дней
        response.addCookie(refreshCookie);
        return ResponseEntity.ok(new AccessTokenDTO(tokenDTO.getAccessToken()));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AccessTokenDTO> refreshToken(@CookieValue String refreshToken, HttpServletResponse response) throws AuthenticationException {
        TokenDTO tokenDTO = authService.refreshToken(refreshToken, response);
        Cookie refreshCookie = new Cookie("refreshToken", tokenDTO.getRefreshToken());
        refreshCookie.setHttpOnly(true); // Запрет доступа из JavaScript
        refreshCookie.setPath("/refresh_token");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // Срок жизни - 7 дней
        response.addCookie(refreshCookie);
        return ResponseEntity.ok(new AccessTokenDTO(tokenDTO.getAccessToken()));
    }
}
