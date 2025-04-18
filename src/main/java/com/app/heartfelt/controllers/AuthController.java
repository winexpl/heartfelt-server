package com.app.heartfelt.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.heartfelt.dtos.AccessTokenDTO;
import com.app.heartfelt.dtos.Password;
import com.app.heartfelt.dtos.TokenDTO;
import com.app.heartfelt.dtos.UserDTO;
import com.app.heartfelt.dtos.UserLoginDTO;
import com.app.heartfelt.dtos.UserRegistrationDTO;
import com.app.heartfelt.services.AuthService;
import com.app.heartfelt.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;



@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> registration(@RequestBody UserRegistrationDTO registrationDto) {
        
        if(userService.existsByUsername(registrationDto.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        UserDTO userDTO = authService.registration(registrationDto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(userDTO.getId())
            .toUri();
        return ResponseEntity.created(location).body(userDTO);
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
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO(tokenDTO.getAccessToken());
        return ResponseEntity.ok(accessTokenDTO);
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

    @PutMapping("/edit_password")
    public ResponseEntity<String> editPassword(@RequestBody Password passwords) {
        System.out.println(passwords);
        try {
            authService.editPassword(passwords);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
        return ResponseEntity.ok("Пароль успешно обновлен");
    }
}
