package com.udoolleh.backend.web;

import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.AdminAuthenticationService;
import com.udoolleh.backend.provider.service.CourseService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestCourse;
import com.udoolleh.backend.web.dto.ResponseCourse;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final AdminAuthenticationService adminAuthenticationService;

    @PostMapping("/course")
    public ResponseEntity<CommonResponse> registerCourse(HttpServletRequest request, @Valid @RequestBody RequestCourse.RegisterCourseDto requestDto){
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }

        courseService.registerCourse(requestDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("여행지 추천 코스 등록 성공")
                .build());
    }

    @GetMapping("/course")
    public ResponseEntity<CommonResponse> getCourseList(){
        List<ResponseCourse.CourseDto> response = courseService.getCourseList();

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("여행지 추천 코스 조회 성공")
                .list(response)
                .build());
    }
    @DeleteMapping("/course/{id}")
    public ResponseEntity<CommonResponse> deleteCourse(HttpServletRequest request, @PathVariable Long id){
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }

        courseService.deleteCourse(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("여행지 추천 코스 삭제 성공")
                .build());
    }
}
