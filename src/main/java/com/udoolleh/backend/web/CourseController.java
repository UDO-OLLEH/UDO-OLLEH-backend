package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.service.CourseService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/course")
    public ResponseEntity<CommonResponse> registerCourse(@Valid @RequestBody RequestCourse.registerDto requestDto){
        courseService.registerCourse(requestDto);
        return new ResponseEntity<>(CommonResponse.builder()
                .message("여행지 상세 등록 성공")
                .build(), HttpStatus.OK);
    }
}
