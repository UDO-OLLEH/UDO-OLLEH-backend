package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.TravelPlace;
import com.udoolleh.backend.provider.service.TravelPlaceService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelPlaceRepository extends JpaRepository<TravelPlace, Long> {

    TravelPlace findByPlaceName(String placeName);
}