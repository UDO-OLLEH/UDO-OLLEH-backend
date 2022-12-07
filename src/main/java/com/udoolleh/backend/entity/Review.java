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
    @Column(name="id")
    private String id = UUID.randomUUID().toString();

    @CreationTimestamp
    @Column(name = "create_at")
    private Date createAt = new Date();

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
    public Review(String context, double grade, User user, Restaurant restaurant){
        this.context = context;
        this.grade = grade;
        this.user = user;
        this.restaurant = restaurant;
    }

    public void updatePhoto(String photo){
        this.photo = photo;
    }

    public void modifyReview(String context, double grade){
        this.context = context;
        this.grade = grade;
    }

}
