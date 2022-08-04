package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Table(name = "review")
@Entity
@Getter
@NoArgsConstructor
public class Review {
    @Id
    @Column(name = "review_id")
    private String reviewId = UUID.randomUUID().toString();

    @CreationTimestamp
    @Column(name = "create_at")
    private Date createAt = new Date();

    @Column(name = "title")
    private String title;

    @Column(name = "context")
    private String context;

    @Column(name = "photo")
    private String photo;

    @Column(name = "grade")
    private Double grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Builder
    public Review(String title, String context, String photo, Double grade, User user, Restaurant restaurant){
        this.title = title;
        this.context = context;
        this.photo = photo;
        this.grade = grade;
        this.user = user;
        this.restaurant = restaurant;
    }

    public void modifyReview(String title, String context, String photo, Double grade){
        this.title = title;
        this.context = context;
        this.photo = photo;
        this.grade = grade;
    }

}
