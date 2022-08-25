package com.udoolleh.backend.entity;

import lombok.Builder;

import javax.persistence.*;
import java.util.UUID;

public class Reply {
    @Id
    @Column(name = "reply_id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "context")
    private String context;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Reply(String context, User user, Board board) {
        this.context = context;
        this.user = user;
        this.board = board;
    }
}
