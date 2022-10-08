package com.udoolleh.backend.entity;

import com.udoolleh.backend.core.type.CourseDetailType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "course_detail")
@Entity
@Getter
@NoArgsConstructor
public class CourseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private CourseDetailType type;

    @Column(name = "context")
    private String context;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_course_id")
    private TravelCourse travelCourse;

    @Builder
    public CourseDetail(CourseDetailType type, String context, TravelCourse travelCourse){
        this.type = type;
        this.context = context;
        this.travelCourse = travelCourse;
    }
}
