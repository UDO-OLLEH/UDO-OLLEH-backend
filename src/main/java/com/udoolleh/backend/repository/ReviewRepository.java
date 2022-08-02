package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.entity.Review;
import com.udoolleh.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByUserAndRestaurant(User user, Restaurant restaurant);
}
