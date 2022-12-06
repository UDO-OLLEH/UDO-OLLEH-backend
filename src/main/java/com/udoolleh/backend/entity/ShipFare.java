package com.udoolleh.backend.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "ship_fare")
@Getter
@Entity
@NoArgsConstructor
public class ShipFare {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "age_group")
    private String ageGroup;

    @Column(name = "round_trip")
    private Integer roundTrip;

    @Column(name = "enter_island")
    private Integer enterIsland;

    @Column(name = "leave_island")
    private Integer leaveIsland;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "harbor_id")
    private Harbor harbor;

    @Builder
    public ShipFare(String ageGroup, Integer roundTrip, Integer enterIsland, Integer leaveIsland, Harbor harbor){
        this.ageGroup = ageGroup;
        this.roundTrip = roundTrip;
        this.enterIsland = enterIsland;
        this.leaveIsland = leaveIsland;
        this.harbor = harbor;
    }
}
