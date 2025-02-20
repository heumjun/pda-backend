package com.denso.pdabackend.response.exception;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * controller에서 발생되는 예외는 @RestControllerAdvice 어노테이션이 정의되어 있는 
 * GlobalException에서 모든 예외처리가 모여 처리되지만
 * jwt의 예외는 controller가 아니기때문에 예외handler를 따로 만들고 exception/jwt controller를 호출하여
 * globalException에서 처리하도록 했음.
 * 
 * 
 * SecurityConfiguration 정의됨.
 * 토큰인증 오류시
 */
 
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.info("exception: CustomAuthenticationEntryPoint");
        response.sendRedirect("/exception/jwt/authentication");
        
        
    }

    
}
