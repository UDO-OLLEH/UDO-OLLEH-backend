package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "board_comment")
@NoArgsConstructor
@Getter
@Entity
public class BoardComment {
    @Id
    @Column(name = "comment_id")
    private String id = UUID.randomUUID().toString();

    @Column
    private String context;

    @Column
    private LocalDateTime createAt = LocalDateTime.now();

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Board board;

    @Builder
    public BoardComment(String context, User user,Board board) {
        this.context = context;
        this.user = user;
        this.board = board;
    }
}
