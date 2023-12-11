package com.walkary.models.dto.request.diary;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class DiaryEdit {
    private LocalDate date;
    private String title;
    private String content;
    private byte[] image;

    @Builder
    public DiaryEdit(LocalDate date, String title, String content, byte[] image) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.image = image;
    }
}
