package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.security.JwtAuthToken;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.ReviewService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestReview;
import com.udoolleh.backend.web.dto.ResponseReview;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @PostMapping("/restaurant/review")
    public ResponseEntity<CommonResponse> registerReview(HttpServletRequest request,
                                                  @RequestPart MultipartFile file,
                                                  @Valid @RequestPart RequestReview.RegisterReviewDto requestDto){
        //유저 확인
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if(token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        //리뷰 등록
        reviewService.registerReview(file, email, requestDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("리뷰 등록 성공")
                .build());
    }

    @PostMapping("/restaurant/review/{id}")
    public ResponseEntity<CommonResponse> updateReview(HttpServletRequest request,
                                                @PathVariable String id,
                                                @RequestPart MultipartFile file,
                                                @Valid @RequestPart RequestReview.UpdateReviewDto requestDto){
        //유저 확인
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if(token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        //리뷰 수정
        reviewService.updateReview(file, email, id, requestDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("리뷰 수정 성공")
                .build());
    }

    @DeleteMapping("/restaurant/review/{id}")
    public ResponseEntity<CommonResponse> deleteReview(HttpServletRequest request, @PathVariable String id){
        //유저 확인
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if(token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        //리뷰 삭제
        reviewService.deleteReview(email, id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("리뷰 삭제 성공")
                .build());
    }
    @GetMapping("/restaurant/{name}/review")
    public ResponseEntity<CommonResponse> getReview(@PathVariable String name){
        List<ResponseReview.ReviewDto> list = reviewService.getReview(name);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("리뷰 조회 성공")
                .list(list)
                .build());
    }
}
