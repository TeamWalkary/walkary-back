package com.walkary.models.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DiaryEditor {
    private LocalDate date;
    private String content;

    @Builder
    public DiaryEditor(LocalDate date, String content) {
        this.date = date;
        this.content = content;
    }
}
