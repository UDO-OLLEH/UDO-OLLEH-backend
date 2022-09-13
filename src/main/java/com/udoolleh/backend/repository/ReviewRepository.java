package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.entity.Review;
import com.udoolleh.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    Review findByUserAndRestaurant(User user, Restaurant restaurant);
    Review findByUserAndId(User user, String id);
    List<Review> findByRestaurant(Restaurant restaurant);
}
