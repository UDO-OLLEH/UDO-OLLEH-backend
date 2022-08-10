package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Menu;
import com.udoolleh.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu findByRestaurantAndName(Restaurant restaurant, String name);
}
