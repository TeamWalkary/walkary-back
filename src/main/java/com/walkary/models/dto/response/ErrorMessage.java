package com.walkary.models.dto.response;

import lombok.Getter;

@Getter
public class ErrorMessage {
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
    }
}
