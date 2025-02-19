package com.honghung.chatapp.component.auth.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {  
        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); 
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("statusCode", 401);
        errorDetails.put("message", "Authentication failed! Reason: Token is invalid or blocked!");
        errorDetails.put("path", "");
        errorDetails.put("timestamp", new Date(System.currentTimeMillis()));
        errorDetails.put("errorCode", "unauthorized");
        String errorJson = new ObjectMapper().writeValueAsString(errorDetails);
        response.getWriter().write(errorJson);
        response.flushBuffer();
    }
    
}
