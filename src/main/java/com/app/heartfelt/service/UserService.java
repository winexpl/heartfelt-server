package com.app.heartfelt.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.app.heartfelt.repository.JpaUserRepository;
import com.app.heartfelt.utils.MappingUtils;
import com.app.heartfelt.dto.QuestionDTO;
import com.app.heartfelt.dto.UserDTO;
import com.app.heartfelt.model.Question;
import com.app.heartfelt.model.User;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private MappingUtils mappingUtils;

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

    public List<UserDTO> findAllByUsername(String username) {
        return userRepository.findAllByUsername(username).stream().map(mappingUtils::convertToDTO).toList();
    }

    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        Class<?> clazz = UserDTO.class;
        Field[] fields = clazz.getDeclaredFields();

        try {
            for(Field field : fields) {
                var value = field.get(userDTO);
                if(value != null) field.set(user, value);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new HttpClientErrorException(HttpStatus.NOT_IMPLEMENTED);
        }
        return mappingUtils.convertToDTO(userRepository.save(user));
    }
    
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
}