package com.school.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.exception.ExceptionLocations;
import com.school.exception.ExceptionResponse;
import com.school.exception.CustomException;
import com.school.message.InternalizationMessageManagerConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;

    @Autowired
    public AuthEntryPointJwt(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ExceptionLocations location;

        if (authException instanceof InsufficientAuthenticationException) {
            location = ExceptionLocations.TOKEN_FORBIDDEN;
        } else {
            location = ExceptionLocations.TOKEN_NOT_FOUND;
        }

        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                authException.getMessage(),
                InternalizationMessageManagerConfig
                        .getExceptionMessage(location.toString()),
                LocalDateTime.now().toString()
        );

        mapper.writeValue(response.getOutputStream(), exceptionResponse);

        throw new CustomException(authException.getMessage(), location);
    }
}

