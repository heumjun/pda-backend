package com.denso.pdabackend.common;

import com.denso.pdabackend.token.dto.UserDto;

public interface AuthenticationFacade {

    UserDto getUserInfo();
    
}
