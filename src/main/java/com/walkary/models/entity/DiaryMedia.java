package com.walkary.models.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class DiaryMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String attachment;

    @ManyToOne
    @JoinColumn(name = "diary_id")
    private Diary diary;
}
