package com.app.heartfelt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.heartfelt.security.filter.JwtFilter;
import com.app.heartfelt.security.handler.CustomAccessDeniedHandler;
import com.app.heartfelt.security.handler.CustomLogoutHandler;
import com.app.heartfelt.security.service.JwtService;
import com.app.heartfelt.service.UserService;

import lombok.AllArgsConstructor;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;



@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    @Autowired
    private final UserService userService;
    @Autowired
    private final CustomLogoutHandler logoutHandler;
    @Autowired
    private final JwtFilter jwtFilter;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAccessDeniedHandler accessDeniedHandler) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // отключение защиты от csrf
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/login/**", "/registration/**", "/refresh_token/**").permitAll()
                .anyRequest().authenticated()
            )
            .userDetailsService(userService)
            .logout(log -> {
                log.logoutUrl("/logout");
                log.addLogoutHandler(logoutHandler);})
            .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accessDeniedHandler))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService)
            .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}