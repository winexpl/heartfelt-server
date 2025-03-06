package com.app.heartfelt.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.repository.JpaUserRepository;
import com.app.heartfelt.dto.UserDTO;
import com.app.heartfelt.dto.UserLoginDTO;
import com.app.heartfelt.dto.UserRegistrationDTO;
import com.app.heartfelt.model.User;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private JpaUserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public UserDTO getUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) return convertToDTO(userOptional.get());
        else return null;
    }

    public UserDTO saveUser(UserRegistrationDTO userDTO) {
        User user = convertToEntity(userDTO);
        try {
            return convertToDTO(userRepository.save(user));
        } catch(DataIntegrityViolationException e) {
            return null;
        }
    }

    public List<UserDTO> getAllByUsername(String username) {
        if(username == null) return userRepository.findAll().stream().map(this::convertToDTO).toList();
        else return userRepository.findAllByUsername(username).stream().map(this::convertToDTO).toList();
    }

    public void updateUser(UUID id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        if(userDTO.getNickname() != null) existingUser.setNickname(userDTO.getNickname());
        userRepository.save(existingUser);
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .nickname(user.getNickname())
            .role(user.getRole()).build();
    }

    private User convertToEntity(UserDTO userDTO) {
        return User.builder()
            .id(userDTO.getId())
            .nickname(userDTO.getNickname())
            .role(userDTO.getRole())
            .username(userDTO.getUsername()).build();
    }

    private User convertToEntity(UserLoginDTO userLoginDTO) {
        return User.builder()
            .username(userLoginDTO.getUsername())
            .password(userLoginDTO.getPassword()).build();
    }

    private User convertToEntity(UserRegistrationDTO userRegistrationDTO) {
        return User.builder()
            .username(userRegistrationDTO.getUsername())
            .nickname(userRegistrationDTO.getNickname())
            .password(userRegistrationDTO.getPassword())
            .role(userRegistrationDTO.getRole()).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
    }
}