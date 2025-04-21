package com.app.heartfelt.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    SUFFERY,
    PSYCHOLOG;

    @Override
    public String getAuthority() {
        return "ROLE_" + toString();
    }
}
