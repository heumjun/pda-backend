package com.denso.pdabackend.response;

import java.util.List;

import com.denso.pdabackend.response.ResponseEntityUtil.FieldError;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data       //@Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode을 한꺼번에 설정
@AllArgsConstructor     //모든 필드 값을 파라미터로 받는 생성자를 만들어줌
@Builder    //builder 패턴사용시
public class DefaultRes<T> {
    
    private int statusCode;
    private boolean status;
    private String message;
    private T data;
    private List<FieldError> errors;
}

