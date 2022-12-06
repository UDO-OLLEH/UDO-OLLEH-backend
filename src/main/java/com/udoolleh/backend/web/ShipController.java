package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.service.ShipService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestHarborTimetable;
import com.udoolleh.backend.web.dto.RequestShipFare;
import com.udoolleh.backend.web.dto.ResponseHarbor;
import com.udoolleh.backend.web.dto.ResponseHarborTimetable;
import com.udoolleh.backend.web.dto.ResponseShipFare;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShipController {
    private final ShipService shipService;

    @PostMapping("/harbor")
    public ResponseEntity<CommonResponse> registerHarbor(@RequestBody Map<String, String> harborName) {
        shipService.registerHarbor(harborName.get("harborName"));

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("항구 등록 성공")
                .build());
    }

    @PostMapping("/harbor/timetable")
    public ResponseEntity<CommonResponse> registerWharfTimetable(@Valid @RequestBody RequestHarborTimetable.RegisterHarborTimetableDto requestDto) {
        shipService.registerHarborTimetable(requestDto.getHarborName(), requestDto.getDestination(), requestDto.getPeriod(), requestDto.getOperatingTime());

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 시간 등록 성공")
                .build());
    }

    @PostMapping("/harbor/ship-fare")
    public ResponseEntity<CommonResponse> registerShipFare(@Valid @RequestBody RequestShipFare.RegisterShipFareDto registerShipFareDto) {
        shipService.registerShipFare(registerShipFareDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 요금 등록 성공")
                .build());
    }

    @GetMapping("/harbor")
    public ResponseEntity<CommonResponse> getHarbors() {
        List<ResponseHarbor.HarborDto> harborDtos = shipService.getAllHarbors();

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("항구 조회 성공")
                .list(harborDtos)
                .build());
    }

    @GetMapping("/harbor/{name}/timetable/{destination}")
    public ResponseEntity<CommonResponse> getHarborTimetable(@PathVariable("name") String name, @PathVariable("destination") String destination) {
        ResponseHarborTimetable.HarborTimetableDto list = shipService.getHarborTimetable(name, destination);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 시간 조회 성공")
                .list(list)
                .build());
    }

    @GetMapping("/harbor/{id}/ship-fare")
    public ResponseEntity<CommonResponse> getShipFare(@PathVariable("id") Long id) {
        ResponseShipFare.HarborShipFare responseHarborShipFare = shipService.getShipFare(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 시간 조회 성공")
                .list(responseHarborShipFare)
                .build());
    }

    @DeleteMapping("/harbor/ship-fare/{id}")
    public ResponseEntity<CommonResponse> deleteShipFare(@PathVariable("id") Long id) {
        shipService.deleteShipFare(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 요금 삭제 성공")
                .build());
    }

    @DeleteMapping("/harbor/{id}")
    public ResponseEntity<CommonResponse> deleteHarbor(@PathVariable("id") Long id) {
        shipService.deleteHarbor(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("항구 삭제 성공")
                .build());
    }

    @DeleteMapping("/harbor/timetable/{id}")
    public ResponseEntity<CommonResponse> deleteWharf(@PathVariable("id") Long id) {
        shipService.deleteHarborTimetable(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 시간 삭제 성공")
                .build());
    }
}
