package com.walkary.models.dto.response.pin;

import java.util.List;


public record MainPinsResponse(
        List<PinResponse> pins
) {}
