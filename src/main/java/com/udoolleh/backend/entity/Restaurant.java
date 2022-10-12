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
    @Column(name="restaurant_id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "place_type")
    private PlaceType placeType;

    @Column(name = "address")
    private String address;

    @Column(name = "total_grade")
    private Double totalGrade;

    @Column(name = "x_coordinate")
    private String xCoordinate;

    @Column(name = "y_coordinate")
    private String yCoordinate;

    @OneToMany(mappedBy = "restaurant")
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photoList = new ArrayList<>();

    @Builder
    public Restaurant(String name, String category,PlaceType placeType, String address, double totalGrade, String xCoordinate, String yCoordinate){
        this.name = name;
        this.category = category;
        this.placeType = placeType;
        this.address = address;
        this.totalGrade = totalGrade;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void addMenu(Menu menu){
        this.menuList.add(menu);
    }

    public void addReview(Review review){
        this.reviewList.add(review);
    }

    public void addPhoto(Photo photo){
        this.photoList.add(photo);
    }

    public void updateGrade(Double grade){ this.totalGrade = grade; }
}
