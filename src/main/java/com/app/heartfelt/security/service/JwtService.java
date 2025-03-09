package com.app.heartfelt.security.service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app.heartfelt.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;


@Service
public class JwtService {
    @Value("${security.jwt.access_token_expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${security.jwt.refresh_token_expiration}")
    private long REFRESH_TOKEN_EXPIRATION;

    @Value("${secure.jwt.secret_key}")
    private String keyString;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(keyString.getBytes());
    }

    public String generateAccessToken(User user) {
        return generateToken(user, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(User user, long tokenExpiration) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("roles", user.getAuthorities());
        claims.put("type", "access");

        return Jwts.builder()
                .setClaims(claims)
                .setAudience("string")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration*1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).requireAudience("string").build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    public boolean validate(String token) {
        boolean isValidToken = isTokenExpired(token);
        return isValidToken;
    }
}
