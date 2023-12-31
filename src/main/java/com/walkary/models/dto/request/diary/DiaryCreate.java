package com.walkary.models.dto.request.diary;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class DiaryCreate {

    private LocalDate date;
    private String title;
    private String content;
    private String image;

    private String userId;

    public DiaryCreate(LocalDate date, String title, String content, String image, String userId) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.image = image;
        this.userId = userId;
    }
}
