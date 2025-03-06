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
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ofNullable(userService.getUserById(id));
    }

    @GetMapping
    public List<UserDTO> getUsers(@RequestParam @Nullable String username) {
        return userService.getAllByUsername(username);
    }
    
    @PostMapping
    public ResponseEntity<UserDTO> registration(@RequestBody UserRegistrationDTO userDTO) {
        UserDTO savedUser = userService.saveUser(userDTO);
        if(savedUser!=null) {
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
            return ResponseEntity.created(location).body(savedUser);
        } else return ResponseEntity.badRequest().body(null);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        userService.updateUser(id, userDTO);
        return ResponseEntity.ok().build();
    }
}