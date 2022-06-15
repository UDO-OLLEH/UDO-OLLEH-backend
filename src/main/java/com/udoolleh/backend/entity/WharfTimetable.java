package com.udoolleh.backend.entity;

import com.udoolleh.backend.core.type.ShipTimetableType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "wharf_timetable")
@Getter
@Entity
@NoArgsConstructor
public class WharfTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "departure_time")
    @Temporal(TemporalType.TIME)
    private Date departureTime;

    @Column(name = "month_type")
    private ShipTimetableType monthType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wharf_name")
    private Wharf wharf;

    @Builder
    public WharfTimetable(Wharf wharf,ShipTimetableType monthType, Date departureTime){
        this.wharf = wharf;
        this.monthType = monthType;
        this.departureTime = departureTime;
    }
}
