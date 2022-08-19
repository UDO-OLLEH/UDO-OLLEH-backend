package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    Restaurant findByName(String restaurantName);
    Page<Restaurant> findAll(Pageable pageable);
}
