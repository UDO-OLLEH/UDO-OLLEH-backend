package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "heart")
@NoArgsConstructor
@Getter
@Entity
public class Heart {
    @Id
    @Column(name = "heart_id")
    public String id = UUID.randomUUID().toString();

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public Board board;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    public User user;

    @Builder
    public Heart(Board board, User user) {
        this.board = board;
        this.user = user;
    }
}
