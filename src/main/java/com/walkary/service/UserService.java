package com.walkary.service;

import com.walkary.config.security.security.jwt.JwtToken;
import com.walkary.models.dto.request.UserLoginRequest;
import com.walkary.models.dto.request.UserSignupRequest;

public interface UserService {

    JwtToken login(UserLoginRequest userDto);

    String signup(UserSignupRequest userDto);
}
