package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.TravelPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPlaceRepository extends JpaRepository<TravelPlace, Long> {
    TravelPlace findByPlaceName(String placeName);
}
