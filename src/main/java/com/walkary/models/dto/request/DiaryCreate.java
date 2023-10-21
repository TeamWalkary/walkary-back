package com.walkary.models.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class DiaryCreate {
    private String content;
    private LocalDate date;

    @Builder
    public DiaryCreate(String content, LocalDate date) {
        this.content = content;
        this.date = date;
    }
}
