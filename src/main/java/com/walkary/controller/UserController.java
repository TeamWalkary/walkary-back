package com.walkary.controller;

import com.walkary.config.security.security.jwt.JwtDto;
import com.walkary.models.dto.MessageResponse;
import com.walkary.models.dto.UserDto;
import com.walkary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody UserDto userDto) {
        return ResponseEntity.ok()
                .body(userService.login(userDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@RequestBody UserDto userDto) {
        String message = userService.signup(userDto);
        if (message.equals("이미 존재하는 아이디입니다.")) {
            return ResponseEntity.badRequest().body(new MessageResponse(message));
        } else {
            return ResponseEntity
                    .ok(new MessageResponse(message));
        }
    }

}
