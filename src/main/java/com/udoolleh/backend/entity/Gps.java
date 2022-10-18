package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "gps")
@Entity
@Getter
@NoArgsConstructor
public class Gps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_course_id")
    private TravelCourse travelCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_place_id")
    private TravelPlace travelPlace;

    @Builder
    public Gps(Double latitude, Double longitude, TravelCourse travelCourse, TravelPlace travelPlace) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.travelCourse = travelCourse;
        this.travelPlace = travelPlace;
    }
}
