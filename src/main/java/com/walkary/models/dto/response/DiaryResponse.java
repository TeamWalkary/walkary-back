package com.walkary.models.dto.response;

import com.walkary.models.entity.Diary;
import com.walkary.models.entity.DiaryMedia;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DiaryResponse {
    private final Long id;
    private final LocalDate date;
    private final String title;
    private final String content;
    private String image;

    public DiaryResponse(Diary diary) {
        this.id = diary.getId();
        this.date = diary.getDate();
        this.title = diary.getTitle();
        this.content = diary.getContent();
    }

    public DiaryResponse(Diary diary, String image) {
        this.id = diary.getId();
        this.date = diary.getDate();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.image = image;
    }

    public DiaryResponse(Diary diary, DiaryMedia diaryMedia) {
        this.id = diary.getId();
        this.date = diary.getDate();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.image = diaryMedia.getAttachment();
    }

    @Builder
    public DiaryResponse(Long id, LocalDate date, String title, String content, String image) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.content = content;
        this.image = image;
    }
}
