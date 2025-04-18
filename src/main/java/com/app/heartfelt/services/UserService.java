
package com.app.heartfelt.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.utils.MappingUtils;
import com.app.heartfelt.dtos.UserDTO;
import com.app.heartfelt.models.entities.User;
import com.app.heartfelt.repositories.JpaUserRepository;
import com.app.heartfelt.security.Role;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private MappingUtils mappingUtils;

    public UserDTO updateRole(UUID id, Role newRole) {
        User user = userRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        user.setRole(Arrays.asList(newRole));
        return mappingUtils.convertToDTO(userRepository.save(user));
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream().map(mappingUtils::convertToDTO).toList();
    }

    public UserDTO findUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) return mappingUtils.convertToDTO(userOptional.get());
        else return null;
    }

    public UserDTO findUserByUsername(String username) {
        return mappingUtils.convertToDTO(loadUserByUsername(username));
    }

    public List<UserDTO> findAllByUsername(String username) {
        return userRepository.findAllByUsername(username).stream().map(mappingUtils::convertToDTO).toList();
    }

    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        updateField(userDTO.getUsername(), user::setUsername);
        updateField(userDTO.getNickname(), user::setNickname);
        updateField(userDTO.getAbout(), user::setAbout);
        
        return mappingUtils.convertToDTO(userRepository.save(user));
    }
    
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public UserDTO findByUsername(String username) {
        return mappingUtils.convertToDTO(loadUserByUsername(username));
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
    }

    public User getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
            && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    private <T> void updateField(T value, Consumer<T> setter) {
        Optional.ofNullable(value).ifPresent(setter);
    }
}