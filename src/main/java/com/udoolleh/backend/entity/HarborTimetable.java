package com.udoolleh.backend.entity;

import com.udoolleh.backend.core.type.ShipTimetableType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "harbor_timetable")
@Getter
@Entity
@NoArgsConstructor
public class HarborTimetable {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "destination")
    private String destination;

    @NotNull
    @Column(name = "period")
    private String period;

    @NotNull
    @Column(name = "operating_time")
    private String operatingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "harbor_id")
    private Harbor harbor;

    @Builder
    public HarborTimetable(String destination, String period, String operatingTime, Harbor harbor){
        this.destination = destination;
        this.period = period;
        this.operatingTime = operatingTime;
        this.harbor = harbor;
    }
}
