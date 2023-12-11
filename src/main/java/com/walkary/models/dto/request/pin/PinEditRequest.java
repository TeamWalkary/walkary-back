package com.walkary.models.dto.request.pin;

public record PinEditRequest(
        Long id,
        String contents,
        Double latitude,
        Double longitude
) {
}
