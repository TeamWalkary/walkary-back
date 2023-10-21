package com.walkary.models.entity;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
public class PointMap {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "seq", nullable = false)
    private Long seq;

    //지오메트리
    @Column
    private Point point;

    @Column
    private Long userId;
    @Column
    private String content;
    @Column
    private String category;
    @Column
    private Timestamp date;

    @Column
    @CreationTimestamp
    private Timestamp createdAt;

    @Column
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "line_map_id")
    private LineMap lineMap;
}
