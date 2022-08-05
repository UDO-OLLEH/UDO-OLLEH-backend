package com.udoolleh.backend.entity;

import com.udoolleh.backend.core.type.PlaceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "restaurant")
@Entity
@Getter
@NoArgsConstructor
public class Restaurant {
    @Id
    @Column(name = "restaurant_id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "place_type")
    private PlaceType placeType;

    @Column(name = "address")
    private String address;

    @Column(name = "total_grade")
    private Float totalGrade;

    @OneToMany(mappedBy = "restaurant")
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviewList = new ArrayList<>();

    @Builder
    public Restaurant(String name, String description,PlaceType placeType, String address, Float totalGrade){
        this.name = name;
        this.description = description;
        this.placeType = placeType;
        this.address = address;
        this.totalGrade = totalGrade;
    }

    public void addMenu(Menu menu){
        this.menuList.add(menu);
    }

    public void addReview(Review review){
        this.reviewList.add(review);
    }
}
