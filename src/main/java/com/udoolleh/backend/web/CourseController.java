package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.service.CourseService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestCourse;
import com.udoolleh.backend.web.dto.ResponseCourse;
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

    @PostMapping("/course")
    public ResponseEntity<CommonResponse> registerCourse(@Valid @RequestBody RequestCourse.RegisterCourseDto requestDto){
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
    public ResponseEntity<CommonResponse> deleteCourse(@PathVariable Long id){
        courseService.deleteCourse(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("여행지 추천 코스 삭제 성공")
                .build());
    }
}
