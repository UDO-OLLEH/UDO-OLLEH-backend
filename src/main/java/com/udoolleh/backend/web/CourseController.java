package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.service.CourseService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestCourse;
import com.udoolleh.backend.web.dto.ResponseCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/course")
    public ResponseEntity<CommonResponse> registerCourse(@Valid @RequestBody RequestCourse.RegisterDto requestDto){
        courseService.registerCourse(requestDto);
        return new ResponseEntity<>(CommonResponse.builder()
                .message("여행지 상세 등록 성공")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/course")
    public ResponseEntity<CommonResponse> getCourses(){
        List<ResponseCourse.GetDto> response = courseService.getCourses();
        return new ResponseEntity<>(CommonResponse.builder()
                .message("여행지 상세 조회 성공")
                .list(response)
                .build(), HttpStatus.OK);
    }

}
