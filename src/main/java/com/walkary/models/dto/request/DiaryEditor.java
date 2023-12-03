package com.walkary.models.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DiaryEditor {
    private LocalDate date;
    private String title;
    private String content;
    private String image;

    @Builder
    public DiaryEditor(LocalDate date, String title, String content, String image) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.image = image;
    }
}
