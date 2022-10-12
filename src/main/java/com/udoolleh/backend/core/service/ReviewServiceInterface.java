package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestReview;
import com.udoolleh.backend.web.dto.ResponseReview;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewServiceInterface {
    void registerReview(MultipartFile file, String email, RequestReview.RegisterReviewDto requestDto);
    void updateReview(MultipartFile file, String email, String reviewId, RequestReview.UpdateReviewDto requestDto);
    void deleteReview(String email, String reviewId);
    List<ResponseReview.ReviewDto> getReview(String restaurantId);
}
