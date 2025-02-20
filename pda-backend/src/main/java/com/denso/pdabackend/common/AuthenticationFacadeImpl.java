package com.denso.pdabackend.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.denso.pdabackend.token.dto.UserDto;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade{

    private Authentication getAutentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserDto getUserInfo() {
        return (UserDto)getAutentication().getPrincipal();
    }
    
}
