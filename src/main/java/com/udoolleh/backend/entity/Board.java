package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "board")
@NoArgsConstructor
@Getter
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY) //즉시 로딩 방식
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Lob
    @Column(name = "context")
    private String context;

    @CreationTimestamp
    @Column(name = "create_at")
    private Date createAt = new Date();

    @Builder
    public Board(Long id, User user, String title, String context) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.context = context;
    }

}
