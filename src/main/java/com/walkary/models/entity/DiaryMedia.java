package com.walkary.models.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class DiaryMedia {
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String attachment;

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
