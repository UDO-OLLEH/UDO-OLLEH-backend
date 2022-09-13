package com.udoolleh.backend.entity;

import lombok.Builder;

import javax.persistence.*;
import java.util.UUID;

public class Like {
    @Id
    @Column(name = "like_id")
    private String id = UUID.randomUUID().toString();

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Like(User user, Board board) {
        this.user = user;
        this.board = board;
    }

}
