package com.app.heartfelt.controllers;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.heartfelt.dtos.UserDTO;
import com.app.heartfelt.security.Role;
import com.app.heartfelt.services.UserService;



@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public String test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(", "));
    }
    
    @PutMapping(value = "/role/{id}")
    public ResponseEntity<UserDTO> putMethodName(@PathVariable UUID id, @RequestParam Role role) {
        System.out.println(role);
        return ResponseEntity.ofNullable(userService.updateRole(id, role));
    }
}
