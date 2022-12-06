package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Harbor;
import com.udoolleh.backend.entity.ShipFare;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipFareRepository extends JpaRepository<ShipFare, Long> {
    ShipFare findByAgeGroupAndHarborId(String ageGroup, Long harborId);
    List<ShipFare> findByHarborId(Long harborId);
}
