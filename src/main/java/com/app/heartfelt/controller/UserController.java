package com.app.heartfelt.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.heartfelt.dto.UserDTO;
import com.app.heartfelt.dto.UserRegistrationDTO;
import com.app.heartfelt.service.UserService;

import jakarta.annotation.Nullable;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable UUID id) {
        return ResponseEntity.ofNullable(userService.findUserById(id));
    }

    @GetMapping
    public List<UserDTO> findUsers(@RequestParam @Nullable String username) {
        if(username==null) return userService.findAllUsers();
        return userService.findAllByUsername(username);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ofNullable(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}