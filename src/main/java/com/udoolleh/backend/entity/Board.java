package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name="board")
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

    @Column(name = "category", length = 5)
    private String category;

    @Lob
    private String content;

    @Column(name = "picture")
    private String picture;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public Board(Long id,String title,String category,String content,String picture){
        this.id=id;
        this.title=title;
        this.category=category;
        this.content=content;
        this.picture=picture;
    }
}
