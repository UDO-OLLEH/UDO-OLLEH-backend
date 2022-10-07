package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "board")
@NoArgsConstructor
@Getter
@Entity
public class Board {
    @Id
    @Column(name = "board_id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Lob
    @Column(name = "context")
    private String context;

    @Column(name = "photo")
    private String photo;

    @Column(name = "countVisit")
    private long countVisit;

    @Column(name = "countLikes")
    private long countLikes;

    @Column(name = "category")
    private String category;

    @CreationTimestamp
    @Column(name = "create_at")
    private Date createAt = new Date();

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Likes> likeList = new ArrayList<>(); //좋아요


    @Builder
    public Board(String title, String context, User user, long countVisit, long countLikes) {
        this.title = title;
        this.context = context;
        this.user = user;
        this.countVisit = countVisit;
        this.countLikes = countLikes;
    }

    public void modifyPosts(String title, String context) {
        this.title = title;
        this.context = context;
    }
    public void updatePhoto(String photo) {
        this.photo = photo;
    }

    public void updateVisit(long countVisit) {
        this.countVisit = countVisit;
    }

    public void updateLikes(long countLikes) {
        this.countLikes = countLikes;
    }
}

