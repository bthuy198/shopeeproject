package com.thuy.shopeeproject.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.security.access.AccessDeniedException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    // @Override
    // public void commence(HttpServletRequest request, HttpServletResponse
    // response,
    // AuthenticationException authException)
    // throws IOException, ServletException {
    // logger.error("Unauthorized error: {}", authException.getMessage());

    // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // final Map<String, Object> body = new HashMap<>();
    // body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    // body.put("error", "Unauthorized");
    // body.put("message", authException.getMessage());
    // body.put("path", request.getServletPath());

    // final ObjectMapper mapper = new ObjectMapper();
    // mapper.writeValue(response.getOutputStream(), body);
    // }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        // 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
    }

    @ExceptionHandler(value = { AccessDeniedException.class })
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        // 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                "Authorization Failed : " + accessDeniedException.getMessage());
    }

    @ExceptionHandler(value = { Exception.class })
    public void commence(HttpServletRequest request, HttpServletResponse response,
            Exception exception) throws IOException {
        // 500
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Internal Server Error : " + exception.getMessage());
    }

}
