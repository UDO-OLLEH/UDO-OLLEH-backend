package com.udoolleh.backend.entity;

import com.udoolleh.backend.core.type.ShipTimetableType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "wharf_timetable")
@Getter
@Entity
@NoArgsConstructor
public class WharfTimetable {
    @Id
    @Column(name="wharf_timetable_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "month_type")
    private ShipTimetableType monthType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wharf_id")
    private Wharf wharf;

    @Builder
    public WharfTimetable(Wharf wharf, ShipTimetableType monthType, String departureTime){
        this.wharf = wharf;
        this.monthType = monthType;
        this.departureTime = departureTime;
    }
}
