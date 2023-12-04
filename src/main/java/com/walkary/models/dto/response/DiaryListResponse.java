package com.walkary.models.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DiaryListResponse {
    private Long id;
    private LocalDate Date;
    private String title;
    private String content;
    private byte[] image;

    @Builder
    public DiaryListResponse(Long id, LocalDate date, String title, String content, byte[] image) {
        this.id = id;
        this.Date = date;
        this.title = title;
        this.content = content;
        this.image = image;
    }
}
