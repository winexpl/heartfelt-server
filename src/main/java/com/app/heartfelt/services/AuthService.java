package com.app.heartfelt.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.dtos.Password;
import com.app.heartfelt.dtos.TokenDTO;
import com.app.heartfelt.dtos.UserDTO;
import com.app.heartfelt.dtos.UserLoginDTO;
import com.app.heartfelt.dtos.UserRegistrationDTO;
import com.app.heartfelt.models.entities.User;
import com.app.heartfelt.repositories.JpaUserRepository;
import com.app.heartfelt.security.Role;
import com.app.heartfelt.security.services.JwtService;
import com.app.heartfelt.utils.MappingUtils;

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

    @Autowired
    private UserService userService;

    public static UUID getCurrentUserId() {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    public void editPassword(Password passwords) {
        User user = userService.getCurrentUserDetails();
        if (!passwordEncoder.matches(passwords.getOldPassword(), user.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Old password isnt correct");
        }
        user.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
        userRepository.save(user);
    }

    public UserDTO registration(UserRegistrationDTO userRegistrationDTO) {
        User user = mappingUtils.convertToEntity(userRegistrationDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(ZonedDateTime.now());
        user.setRole(List.of(Role.SUFFERY));
        user = userRepository.save(user);
        return mappingUtils.convertToDTO(user);
    }

    public TokenDTO authenticate(UserLoginDTO userLoginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getUsername(),
                        userLoginDTO.getPassword()));

        User user = userRepository.findByUsername(userLoginDTO.getUsername()).orElseThrow();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenDTO(accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken, HttpServletResponse response) throws AuthenticationException {
        if (refreshToken == null) {
            throw new BadCredentialsException("No refresh token");
        }
        ;
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
