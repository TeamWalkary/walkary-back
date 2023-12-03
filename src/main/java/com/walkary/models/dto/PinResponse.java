package com.walkary.models.dto;

import org.springframework.format.annotation.DateTimeFormat;

public record PinResponse(
        Long id,
        String contents,
        Double latitude,
        Double longitude,
        @DateTimeFormat(pattern = "HH:mm")
        String stampTime
) { }