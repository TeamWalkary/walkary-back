package com.walkary.models.entity;

import com.walkary.models.dto.request.DiaryEditor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class DiaryMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String attachment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Builder
    public DiaryMedia(Long id, String attachment, Diary diary) {
        this.id = id;
        this.attachment = attachment;
        this.diary = diary;
    }

    public void edit(String image){
        attachment = image;
    }
}
