package com.walkary.controller;

import com.walkary.config.security.security.jwt.JwtDto;
import com.walkary.models.dto.UserDto;
import com.walkary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestParam UserDto userDto) {
        JwtDto jwt = userService.login(userDto);
        return ResponseEntity
                .ok()
                .header("Authorization", "Bearer " + jwt.accessToken())
                .build();
    }

}
