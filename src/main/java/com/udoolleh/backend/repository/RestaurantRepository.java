package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    Restaurant findByName(String restaurantName);
}
