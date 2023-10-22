package com.walkary.models.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private String id;

    @Column
    private String password;
    @Column
    private String nickname;

    @Column
    @CreationTimestamp
    private Timestamp createdAt;

    @Column
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Builder
    public User(String password, String nickname) {
        this.password = password;
        this.nickname = nickname;
    }
}
