package com.walkary.models.dto.response.pin;

import org.springframework.format.annotation.DateTimeFormat;

public record AllDatePinResponse(
        Long id,
        String contents,
        Double latitude,
        Double longitude,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        String date,
        @DateTimeFormat(pattern = "HH:mm")
        String stampTime
){}
