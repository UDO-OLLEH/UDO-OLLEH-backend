package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "USER")
@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "refreshToken")
    private String refreshToken;

    @Column(name = "salt")
    private String salt;


    @Builder
    public User(String email, String password, String salt){
        this.email = email;
        this.password = password;
        this.salt = salt;
    }
    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
