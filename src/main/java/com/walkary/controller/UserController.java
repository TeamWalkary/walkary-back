package com.walkary.controller;

import com.walkary.config.security.security.jwt.JwtToken;
import com.walkary.models.dto.MessageResponse;
import com.walkary.models.dto.request.UserLoginRequest;
import com.walkary.models.dto.request.UserSignupRequest;
import com.walkary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/apis")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@Valid @RequestBody UserLoginRequest loginUser) {
        return ResponseEntity.ok().body(userService.login(loginUser));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody UserSignupRequest userDto) {
        return ResponseEntity.ok(new MessageResponse(userService.signup(userDto)));
    }

}
