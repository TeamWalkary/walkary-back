package com.walkary.models.dto.response.calendar;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CalendarResponse {
    private final String day;

    @Builder
    public CalendarResponse(String day) {
        this.day = day;
    }
}
