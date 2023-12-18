package com.walkary.config.security.security.jwt;

public record JwtToken(
        String accessToken,
        String refreshToken
){
}
