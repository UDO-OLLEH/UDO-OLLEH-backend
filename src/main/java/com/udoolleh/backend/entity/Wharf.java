package com.udoolleh.backend.entity;

import com.udoolleh.backend.core.type.ShipCourseType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "wharf")
@Getter
@Entity
@NoArgsConstructor
public class Wharf {
    @Id
    @Column(name="wharf_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wharf_course")
    private ShipCourseType wharfCourse;

    @OneToMany(mappedBy = "wharf", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WharfTimetable> wharfTimetableList = new ArrayList<>(); // 시간표 리스트

    @Builder
    public Wharf(ShipCourseType wharfCourse){
        this.wharfCourse = wharfCourse;
        }

    public void addTimetable(WharfTimetable wharfTimetable){
        this.wharfTimetableList.add(wharfTimetable);
    }
}
