package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "travel_place")
@Entity
@Getter
@NoArgsConstructor
public class TravelPlace {

    @Id
    @Column(name = "travel_place_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "photo")
    private String photo;

    @Column(name = "context")
    private String context;

    @Builder
    public TravelPlace(String placeName, String context) {
        this.placeName = placeName;
        this.context = context;
    }

    public void updatePhoto(String photo) {
        this.photo = photo;
    }

    public void updatePlace(String placeName, String context) {
        this.placeName = placeName;
        this.context = context;
    }
}
