package com.app.heartfelt.security.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.warn("Access denied for request: " + request.getRequestURI());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }
}
