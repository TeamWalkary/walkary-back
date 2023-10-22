package com.walkary.models.dto;

import java.util.List;


public record MainPinsResponse(
        List<PinResponse> pins
) {}
