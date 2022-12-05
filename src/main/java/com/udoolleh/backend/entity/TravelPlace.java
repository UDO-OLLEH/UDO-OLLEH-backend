package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "intro")
    private String intro;

    @Column(name = "context", columnDefinition = "LONGTEXT")
    private String context;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "travelPlace", cascade = CascadeType.REMOVE)
    private List<Gps> gpsList = new ArrayList<>();

    @Builder
    public TravelPlace(String placeName, String intro, String context) {
        this.placeName = placeName;
        this.intro = intro;
        this.context = context;
    }

    public void updatePhoto(String photo) {
        this.photo = photo;
    }

    public void updatePlace(String placeName, String context) {
        this.placeName = placeName;
        this.context = context;
    }

    public void addGps(Gps gps) {
        this.gpsList.add(gps);
    }
}
