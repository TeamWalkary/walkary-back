package com.walkary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkary.models.dto.request.UserLoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login() throws Exception {
        String username = "testuser";
        String password = "password";
        UserLoginRequest userLoginRequest = new UserLoginRequest(
                username,
                password
        );

        String value = objectMapper.writeValueAsString(userLoginRequest);
        mockMvc.perform(
                post("/apis/login")
                        .content(value)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}