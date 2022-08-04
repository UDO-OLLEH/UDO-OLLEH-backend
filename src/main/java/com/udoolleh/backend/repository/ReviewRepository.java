package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.entity.Review;
import com.udoolleh.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
    Review findByUserAndRestaurant(User user, Restaurant restaurant);
    Review findByUserAndReviewId(User user, String reviewId);
}
