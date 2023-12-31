package com.walkary.models.entity;

import com.walkary.models.dto.request.pin.PinEditor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "point_map")
public class PointMap {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Point point;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column
    private String content;
    @Column
    private String category;
    @Column
    private LocalDate date;

    @Column
    @CreationTimestamp
    private Timestamp createdAt;

    @Column
    @UpdateTimestamp
    private Timestamp updatedAt;


    public PinEditor.PinEditorBuilder toEditor() {
        return PinEditor.builder()
                .content(content)
                .point(point);
    }

    public void edit(PinEditor build) {
        content = build.getContent();
        point = build.getPoint();
    }
}
