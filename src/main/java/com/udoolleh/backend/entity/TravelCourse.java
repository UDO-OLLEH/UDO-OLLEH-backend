package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "travel_course")
@Entity
@Getter
@NoArgsConstructor
public class TravelCourse {
    @Id
    @Column(name = "travel_course_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course")
    private String course;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "travelCourse", cascade = CascadeType.REMOVE)
    private List<CourseDetail> detailList = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "travelCourse", cascade = CascadeType.REMOVE)
    private List<Gps> gpsList = new ArrayList<>();

    @Builder
    public TravelCourse(String courseName, String course){
        this.courseName = courseName;
        this.course = course;
    }

    public void addDetail(CourseDetail detail){
        this.detailList.add(detail);
    }

    public void addGps(Gps gps){
        this.gpsList.add(gps);
    }
}
