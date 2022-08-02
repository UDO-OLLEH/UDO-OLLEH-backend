package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestReviewDto;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewServiceInterface {
    void registerReview(MultipartFile file, String email, RequestReviewDto.register requestDto);
}
