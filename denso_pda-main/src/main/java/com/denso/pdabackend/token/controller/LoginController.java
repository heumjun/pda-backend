package com.denso.pdabackend.token.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denso.pdabackend.response.ResponseEntityUtil;
import com.denso.pdabackend.response.exception.BusinessException;
import com.denso.pdabackend.token.dto.UserDto;
import com.denso.pdabackend.token.service.ManagerDetailsServiceImpl;
import com.denso.pdabackend.token.util.JwtTokenProvider;
import com.denso.pdabackend.utils.DensoStringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("login")
public class LoginController {

    private final ManagerDetailsServiceImpl managerDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    
    @PostMapping
    public ResponseEntity<?> login(@RequestBody UserDto params) {

        Map<String,Object> data = new HashMap<String,Object>();
        UserDetails userInfo = null;

        userInfo =  managerDetailsService.loadUserByUsername(params); 
        
//        if(!userInfo.getPassword().equals(DensoStringUtils.encryptSHA256(params.getPassword()))){
//            throw new BusinessException("비밀번호가 틀립니다.");
//        }

        //토큰생성시 userDto를 넘겨줘야하기때문에 userDetails 정보에서 dto 로 변환
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userInfo, userDto);   //객체 복사

        String token = jwtTokenProvider.createAccessToken(userDto);
        
        data.put("token", token);

        return ResponseEntityUtil.ok(data);
    }
    
     
    
}
