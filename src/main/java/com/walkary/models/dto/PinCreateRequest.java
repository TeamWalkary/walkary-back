package com.walkary.models.dto;

public record PinCreateRequest(
        String contents,
        Double latitude,
        Double longitude
) { }
