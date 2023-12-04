package com.walkary.models.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Data
@NoArgsConstructor
public class DiaryWithAttachmentDTO {

    private Long id;
    private LocalDate date;
    private String title;
    private String content;
    private byte[] attachment;

    @Builder
    public DiaryWithAttachmentDTO(Long id, LocalDate date, String title, String content, byte[] attachment) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.content = content;
        this.attachment = attachment;
    }
}
