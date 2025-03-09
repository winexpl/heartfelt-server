package com.app.heartfelt.service;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.heartfelt.dto.TokenDTO;
import com.app.heartfelt.dto.UserLoginDTO;
import com.app.heartfelt.dto.UserRegistrationDTO;
import com.app.heartfelt.model.User;
import com.app.heartfelt.repository.JpaUserRepository;
import com.app.heartfelt.security.service.JwtService;
import com.app.heartfelt.utils.MappingUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {
    @Autowired
    private MappingUtils mappingUtils;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void registration(UserRegistrationDTO userRegistrationDTO) {
        User user = mappingUtils.convertToEntity(userRegistrationDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
    }

    public TokenDTO authenticate(UserLoginDTO userLoginDTO) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userLoginDTO.getUsername(),
                userLoginDTO.getPassword()
            )
        );

        User user = userRepository.findByUsername(userLoginDTO.getUsername()).orElseThrow();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenDTO(accessToken, refreshToken);
    }

    public TokenDTO refreshToken(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("No JWT token");
        }
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user found"));
        if (jwtService.validate(token)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return new TokenDTO(accessToken, refreshToken);
        }
        return null;
    }
}
