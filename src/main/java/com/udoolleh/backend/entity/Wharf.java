package com.udoolleh.backend.entity;

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
    @Column(name = "wharf_name")
    private String wharf;

    @OneToMany(mappedBy = "wharf", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WharfTimetable> wharfTimetableList = new ArrayList<>(); // 시간표 리스트

    @Builder
    public Wharf(String wharf){
        this.wharf = wharf;
    }

    public void addTimetable(WharfTimetable wharfTimetable){
        this.wharfTimetableList.add(wharfTimetable);
    }
}
