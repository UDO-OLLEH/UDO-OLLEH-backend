package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "refreshToken")
    private String refreshToken;

    @Column(name = "profile")
    private String profile;

    @Column(name = "salt")
    private String salt;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();


    @Builder
    public User(String email, String password, String nickname, String salt) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.salt = salt;
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void addBoard(Board board) {
        this.boardList.add(board);
    }

    public void addReview(Review review) {
        this.reviewList.add(review);
    }
    public void changeUserInfo(String password, String nickname, String salt, String profile){
        this.password = password;
        this.nickname = nickname;
        this.salt = salt;
        this.profile = profile;
    }

}
