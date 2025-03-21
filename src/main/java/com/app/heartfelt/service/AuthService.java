package com.app.heartfelt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
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
        System.out.println(user.toString());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenDTO(accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken, HttpServletResponse response) throws AuthenticationException {
        if (refreshToken == null) {
            throw new BadCredentialsException("No refresh token");
        };
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user found"));
        if (jwtService.validate(refreshToken)) {
            String accessToken = jwtService.generateAccessToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
            return new TokenDTO(accessToken, newRefreshToken);
        }
        return null;
    }
}
