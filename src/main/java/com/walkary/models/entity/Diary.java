package com.walkary.models.entity;

import com.walkary.models.dto.request.DiaryEdit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private LocalDate date;

    @Column
    private String content;

    @Column
    @CreationTimestamp
    private Timestamp createdAt;

    @Column
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Builder
    public Diary(LocalDate date, String content) {
        this.date = date;
        this.content = content;
    }

    public DiaryEdit.DiaryEditBuilder toEditor() {
        return DiaryEdit.builder()
                .date(date)
                .content(content);
    }

    public void edit(DiaryEdit diaryEditor) {
        date = diaryEditor.getDate();
        content = diaryEditor.getContent();
    }
}
