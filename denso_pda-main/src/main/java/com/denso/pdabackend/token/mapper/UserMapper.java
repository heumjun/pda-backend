package com.denso.pdabackend.token.mapper;

import java.util.Optional;

import com.denso.pdabackend.token.dto.UserDto;

public interface UserMapper {

    Optional<UserDto> findByMangerId(UserDto params);

}
