package com.udoolleh.backend.web;

import com.amazonaws.Response;
import com.udoolleh.backend.core.type.ShipCourseType;
import com.udoolleh.backend.core.type.ShipTimetableType;
import com.udoolleh.backend.exception.errors.NotFoundWharfException;
import com.udoolleh.backend.provider.service.ShipService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestWharfTimetable;
import com.udoolleh.backend.web.dto.ResponseWharfTimetable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShipController {
    private final ShipService shipService;

    @PostMapping("/udo/wharf")
    public ResponseEntity<CommonResponse> registerWharf(@RequestBody Map<String, ShipCourseType> wharfCourse){
        shipService.registerWharfCourse(wharfCourse.get("wharfCourse"));

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("선착장 코스 등록 성공")
                .build());
    }

    @PostMapping("/udo/wharf/timetable")
    public ResponseEntity<CommonResponse> registerWharfTimetable(@Valid @RequestBody RequestWharfTimetable.RegisterWharfTimeDto requestDto){
        shipService.registerWharfTimetable(requestDto.getWharfCourse(), requestDto.getDepartureTime(), requestDto.getMonthType());

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 시간 등록 성공")
                .build());
    }

    @GetMapping("/udo/wharf")
    public ResponseEntity<CommonResponse> getWharfList(){
        List<String> wharfList = shipService.getAllWharf().orElseGet(()-> null);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("선착장 조회 성공")
                .list(wharfList)
                .build());
    }

    @GetMapping("/udo/wharf/timetable")
    public ResponseEntity<CommonResponse> getWharfTimetable(@RequestParam ShipCourseType wharfCourse, @RequestParam ShipTimetableType monthType){
        ResponseWharfTimetable.WharfTimetableDto list = shipService.getWharfTimetable(wharfCourse, monthType).orElseGet(()->null);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배시간 조회 성공")
                .list(list)
                .build());
    }

    @DeleteMapping("/udo/wharf/{wharfCourse}")
    public ResponseEntity<CommonResponse> deleteWharf(@PathVariable("wharfCourse") ShipCourseType wharfCourse){
        shipService.deleteWharf(wharfCourse);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("선착장 코스 삭제 성공")
                .build());

    }
    @DeleteMapping("/udo/wharf/timetable/{wharfCourse}/{monthType}")
    public ResponseEntity<CommonResponse> deleteWharf(@PathVariable("wharfCourse") ShipCourseType wharfCourse, @PathVariable("monthType") ShipTimetableType monthType){
        shipService.deleteWharfTimetable(wharfCourse, monthType);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 시간 삭제 성공")
                .build());
    }

}
