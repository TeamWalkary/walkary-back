package com.walkary.models.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String token;

    @Column
    @CreationTimestamp
    private Timestamp createdAt;

    @Column
    @UpdateTimestamp
    private Timestamp updatedAt;

    public static UserEntity login(String id, String password) {
        UserEntity user = new UserEntity();
        user.id = id;
        user.password = password;
        return user;
    }

    public static UserEntity signup(String id, String password, String nickname, String email, String phone) {
        UserEntity user = new UserEntity();
        user.id = id;
        user.password = password;
        user.nickname = nickname;
        user.email = email;
        user.phone = phone;
        return user;
    }
}
