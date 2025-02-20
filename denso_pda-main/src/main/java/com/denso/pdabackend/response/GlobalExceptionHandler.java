package com.denso.pdabackend.response;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.denso.pdabackend.response.exception.BusinessException;
import com.denso.pdabackend.response.exception.UnathorizedException;

import lombok.extern.slf4j.Slf4j;

/**
 * 예외처리 전역처리
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * binding error 발생시
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<?> handleBindException(BindException e){
        log.error("handleBindException");
        log.error(e.getBindingResult().toString());
        return ResponseEntityUtil.error(StatusCode.BAD_REQUEST,e.getBindingResult());
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        log.error("handleHttpRequestMethodNotSupportedException");
        return ResponseEntityUtil.error(StatusCode.METHOD_NOT_ALLOWED);
    }
    /**
     * 존재하지 않는 URI 접근시
     * @param e
     * @return
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException e){
        log.error("handleNoResourceFoundException");
        return ResponseEntityUtil.error(StatusCode.NOT_FOUND);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생함 (로그인)
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e){
        log.error("handleAccessDeniedException");
        return ResponseEntityUtil.error(StatusCode.FORBIDDEN);
    }

    /**
     * 토큰인증 안됨.
     * @param e
     * @return
     */
    @ExceptionHandler(UnathorizedException.class)  //예외클래스 따로 만듬.
    protected ResponseEntity<?> handleUnathorizedException(UnathorizedException e){
        log.error("handleUnathorizedException");
        return ResponseEntityUtil.error(StatusCode.UNAUTHORIZED);
    }

    /**
     * 사용자 정의 예외처리
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<?> handleBusinessException(BusinessException e){
        log.error("handleBusinessException", e.getMessage());
        return ResponseEntityUtil.error(StatusCode.NO_CONTENT,e.getMessage());

    }


    /**
     * 위 예외처리 외 모든 예외
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e){
        log.error("handleException", e);
        return ResponseEntityUtil.error(StatusCode.INTERNAL_SERVER_ERROR);
    }



}
