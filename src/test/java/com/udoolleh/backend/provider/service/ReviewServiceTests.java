package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.entity.Review;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.NotFoundReviewException;
import com.udoolleh.backend.exception.errors.ReviewDuplicatedException;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.repository.ReviewRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestReviewDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ReviewServiceTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewService reviewService;

    @Test
    @Transactional
    @DisplayName("리뷰 등록 테스트(성공)")
    void registerReviewTest(){
        User user = User.builder()
                .email("test")
                .password("1234")
                .build();
        user = userRepository.save(user);

        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        //리뷰 등록
        RequestReviewDto.register requestDto = RequestReviewDto.register.builder()
                .restaurantId(restaurant.getId())
                .title("제목")
                .context("리뷰 내용")
                .grade(3.5)
                .build();
        reviewService.registerReview(null, "test", requestDto);

        assertNotNull(reviewRepository.findByUserAndRestaurant(user, restaurant));
    }
    @Test
    @Transactional
    @DisplayName("리뷰 등록 테스트(실패 - 이미 리뷰가 있는 경우)")
    void registerReviewTestWhenExistReview(){
        User user = User.builder()
                .email("test")
                .password("1234")
                .build();
        user = userRepository.save(user);

        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        //리뷰 등록
        RequestReviewDto.register requestDto = RequestReviewDto.register.builder()
                .restaurantId(restaurant.getId())
                .title("제목")
                .context("리뷰 내용")
                .grade(3.5)
                .build();
        reviewService.registerReview(null, "test", requestDto);

        //리뷰 중복
        assertThrows(ReviewDuplicatedException.class, ()-> reviewService.registerReview(null, "test", requestDto));
    }
    @Test
    @Transactional
    @DisplayName("리뷰 수정 테스트(성공)")
    void modifyReviewTest(){
        User user = User.builder()
                .email("test")
                .password("1234")
                .build();
        user = userRepository.save(user);

        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        //리뷰 등록
        RequestReviewDto.register requestDto = RequestReviewDto.register.builder()
                .restaurantId(restaurant.getId())
                .title("제목")
                .context("리뷰 내용")
                .grade(3.5)
                .build();
        reviewService.registerReview(null, "test", requestDto);

        Review review = reviewRepository.findByUserAndRestaurant(user, restaurant);
        //리뷰 수정
        RequestReviewDto.modify request = RequestReviewDto.modify.builder()
                .title("수정한 제목")
                .context("리뷰 수정 내용")
                .grade(5.0)
                .build();
        reviewService.modifyReview(null, "test", review.getReviewId(), request);

        Review result = reviewRepository.findByUserAndRestaurant(user, restaurant);
        assertTrue(result.getTitle().equals("수정한 제목"));
    }

    @Test
    @Transactional
    @DisplayName("리뷰 수정 테스트(실패 - 리뷰가 없을 경우)")
    void modifyReviewTestWhenNotExistReview(){
        User user = User.builder()
                .email("test")
                .password("1234")
                .build();
        user = userRepository.save(user);

        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        //리뷰 수정
        RequestReviewDto.modify request = RequestReviewDto.modify.builder()
                .title("수정한 제목")
                .context("리뷰 수정 내용")
                .grade(5.0)
                .build();
        assertThrows(NotFoundReviewException.class, ()-> reviewService.modifyReview(null, "test", "옳지 않은 리뷰 아이디", request));
    }

    @Test
    @Transactional
    @DisplayName("리뷰 삭제 테스트(성공)")
    void deleteReviewTest(){
        User user = User.builder()
                .email("test")
                .password("1234")
                .build();
        user = userRepository.save(user);

        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);
        //리뷰 등록
        RequestReviewDto.register requestDto = RequestReviewDto.register.builder()
                .restaurantId(restaurant.getId())
                .title("제목")
                .context("리뷰 내용")
                .grade(3.5)
                .build();
        reviewService.registerReview(null, "test", requestDto);
        Review review = reviewRepository.findByUserAndRestaurant(user, restaurant);

        //리뷰 삭제
        reviewService.deleteReview("test", review.getReviewId());

        assertNull(reviewRepository.findByUserAndRestaurant(user, restaurant));
        assertFalse(user.getReviewList().contains(review));
        assertFalse(restaurant.getReviewList().contains(review));
    }
    @Test
    @Transactional
    @DisplayName("리뷰 삭제 테스트(실패 - 리뷰가 없을 경우)")
    void deleteReviewTestWhenNotExistReview() {
        User user = User.builder()
                .email("test")
                .password("1234")
                .build();
        user = userRepository.save(user);

        Restaurant restaurant = Restaurant.builder()
                .name("음식점")
                .build();
        restaurant = restaurantRepository.save(restaurant);
        //리뷰 삭제
        assertThrows(NotFoundReviewException.class, ()-> reviewService.deleteReview("test", "존재하지 않은 리뷰 아이디"));
    }
    }
