package com.walkary.models.entity;

import com.walkary.models.dto.request.DiaryEditor;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Builder
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(mappedBy = "diary")
    private DiaryMedia diaryMedia;

    @Column
    private LocalDate date;

    @Column
    private String title;

    @Column(length = 500)
    private String content;

    @Column
    @CreationTimestamp
    private Timestamp createdAt;

    @Column
    @UpdateTimestamp
    private Timestamp updatedAt;

    public Diary(LocalDate date, String title, String content, UserEntity user) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public DiaryEditor.DiaryEditorBuilder toEditor() {
        return DiaryEditor.builder()
                .date(date)
                .title(title)
                .content(content);

    }

    public void edit(DiaryEditor diaryEditor) {
        date = diaryEditor.getDate();
        title = diaryEditor.getTitle();
        content = diaryEditor.getContent();
    }
}
