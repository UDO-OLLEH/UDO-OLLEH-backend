package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestReview;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewServiceInterface {
    void registerReview(MultipartFile file, String email, RequestReview.registerDto requestDto);
    void modifyReview(MultipartFile file, String email, String reviewId, RequestReview.modifyDto requestDto);
    void deleteReview(String email, String reviewId);
}
