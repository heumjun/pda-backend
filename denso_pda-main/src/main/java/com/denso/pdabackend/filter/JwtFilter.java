package com.denso.pdabackend.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.denso.pdabackend.token.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{
    
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
        
        String token = jwtTokenProvider.resolveToken(request);
		
		if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
			
			//유효한 토큰이라면 토큰에서 유저정보 받아옴
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			
			//securityContext에 Authentication 객체 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("Secutiry context에 인증정보 저장:"+ request.getRequestURI());
						
		}else {
			log.debug("유효한 Jwt토큰이 없습니다.:"+ request.getRequestURI());
			
		}
		filterChain.doFilter(request, response);
    }
    
}