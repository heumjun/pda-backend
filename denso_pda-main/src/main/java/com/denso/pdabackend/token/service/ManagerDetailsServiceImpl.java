package com.denso.pdabackend.token.service;

import java.util.Arrays;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.denso.pdabackend.token.dto.UserDto;
import com.denso.pdabackend.token.mapper.UserMapper;
import com.denso.pdabackend.response.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerDetailsServiceImpl implements UserDetailsService{

    private final UserMapper userMapper;

    
    public UserDetails loadUserByUsername(UserDto params) throws UsernameNotFoundException {
        
        if(ObjectUtils.isEmpty(params.getUserId())) {
            throw new BusinessException("ID를 입력 하세요.");
		}
        UserDto user = userMapper.findByMangerId(params)
                            .orElseThrow(()->new BusinessException("ID가 존재하지 않습니다."));

        user.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(user.getRole())));
        
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

    

}
