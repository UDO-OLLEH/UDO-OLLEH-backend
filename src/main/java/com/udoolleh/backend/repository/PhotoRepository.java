package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Photo;
import com.udoolleh.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByRestaurant(Restaurant restaurant);
}
