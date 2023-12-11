package com.walkary.models.dto.request.pin;

public record PinCreateRequest(
        String contents,
        Double latitude,
        Double longitude
) { }
