package com.walkary.models.dto.request;

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

    private String content;

    @Builder
    public DiaryEdit(LocalDate date, String content) {
        this.date = date;
        this.content = content;
    }
}
