package com.walkary.models.dto.response;

import com.walkary.models.entity.Diary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DiaryResponse {
    private final Long id;
    private final LocalDate date;
    private final String content;

    public DiaryResponse(Diary diary){
        this.id = diary.getId();
        this.date = diary.getDate();
        this.content = diary.getContent();
    }

    @Builder
    public DiaryResponse(Long id, LocalDate date, String content) {
        this.id = id;
        this.date = date;
        this.content = content;
    }
}
