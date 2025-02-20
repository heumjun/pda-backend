package com.denso.pdabackend.token.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.response.exception.UnathorizedException;

import io.swagger.v3.oas.annotations.Operation;

/**
 * controller 진입전 filter영역에서 발생하는 exception을 처리 하기 위함.
 */

@RestController
@RequestMapping("/exception/jwt")
public class ExceptionHandleController {
    
    /**
     * 토큰인증 예외처리
     */
    @GetMapping("/authentication")
    @Operation(summary = "토큰인증 예외처리", description = "filter에서 토큰인증 실패시 호출")
    public void authenticationException(){
        throw new UnathorizedException();
    }
    
    /**
     * 비인가 예외처리 (role 안맞을때)
     */
    @GetMapping("/accessDenied")
    @Operation(summary = "비인가 예외처리", description = "filter에서 role불만족시")
    public void accessDeniedException() {
    	throw new AccessDeniedException("");
    }
}
