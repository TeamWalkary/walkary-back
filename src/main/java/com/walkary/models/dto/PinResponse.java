package com.walkary.models.dto;

public record PinResponse(
        Long id,
        String contents,
        Double latitude,
        Double longitude
) {
}
