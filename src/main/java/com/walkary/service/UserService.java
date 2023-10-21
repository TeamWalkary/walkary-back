package com.walkary.service;

import com.walkary.config.security.security.jwt.JwtDto;
import com.walkary.models.dto.UserDto;

public interface UserService {

    JwtDto login(UserDto userDto);
}
