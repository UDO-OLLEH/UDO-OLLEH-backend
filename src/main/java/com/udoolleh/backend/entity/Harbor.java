package com.udoolleh.backend.entity;

import com.udoolleh.backend.core.type.ShipCourseType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Table(name = "harbor")
@Getter
@Entity
@NoArgsConstructor
public class Harbor {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "harbor_name")
    private String harborName;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "harbor", cascade = CascadeType.REMOVE)
    private List<HarborTimetable> harborTimetables = new ArrayList<>(); // 시간표 리스트

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "harbor", cascade = CascadeType.REMOVE)
    private List<ShipFare> shipFares = new ArrayList<>();

    @Builder
    public Harbor(String harborName) {
        this.harborName = harborName;
    }

    public void addHarborTimetable(HarborTimetable harborTimetable) {
        this.harborTimetables.add(harborTimetable);
    }

    public void addShipFare(ShipFare shipFare) {
        this.shipFares.add(shipFare);
    }
}
