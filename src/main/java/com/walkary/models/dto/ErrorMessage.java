package com.walkary.models.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorMessage {
    private String message;

    @Builder
    public ErrorMessage(String message) {
        this.message = message;
    }
}
