package com.walkary.models.dto.response.pin;

import java.util.List;
import java.util.Map;

public record AllMainPinsResponse(
        Map<String, List<AllDatePinResponse>> pins
) {}
