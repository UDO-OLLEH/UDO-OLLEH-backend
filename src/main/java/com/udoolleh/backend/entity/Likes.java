package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "likes")
@NoArgsConstructor
@Getter
@Entity
public class Likes {
    @Id
    @Column(name = "likes_id")
    public String id = UUID.randomUUID().toString();

    @Column(name = "count")
    public Long count;

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    public Board board;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @Builder
    public Likes(Board board, User user) {
        this.board = board;
        this.user = user;
    }
}
