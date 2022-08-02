package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.security.JwtAuthToken;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.ReviewService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @PostMapping("/review")
    ResponseEntity<CommonResponse> registerReview(HttpServletRequest request,
                                                  @RequestPart MultipartFile file,
                                                  @Valid @RequestPart RequestReviewDto.register requestDto){
        //유저 확인
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if(token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        //리뷰 등록
        reviewService.registerReview(file, email, requestDto);

        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("리뷰 등록 성공")
                .build(), HttpStatus.OK);
    }
}
