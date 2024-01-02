package com.walkary.models.dto.response.calendar;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CalendarResponse {
    private final String diaryDay;
    private final String pinDay;

    @Builder
    public CalendarResponse(String diaryDay, String pinDay) {
        this.diaryDay = diaryDay;
        this.pinDay = pinDay;
    }
}
