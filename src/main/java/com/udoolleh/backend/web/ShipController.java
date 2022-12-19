package com.udoolleh.backend.web;

import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.AdminAuthenticationService;
import com.udoolleh.backend.provider.service.ShipService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestHarborTimetable;
import com.udoolleh.backend.web.dto.RequestShipFare;
import com.udoolleh.backend.web.dto.ResponseHarbor;
import com.udoolleh.backend.web.dto.ResponseHarborTimetable;
import com.udoolleh.backend.web.dto.ResponseShipFare;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShipController {
    private final ShipService shipService;
    private final AdminAuthenticationService adminAuthenticationService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @PostMapping("/harbor")
    public ResponseEntity<CommonResponse> registerHarbor(HttpServletRequest request, @RequestBody Map<String, String> harborName) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }

        shipService.registerHarbor(harborName.get("harborName"));

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("항구 등록 성공")
                .build());
    }

    @PostMapping("/harbor/timetable")
    public ResponseEntity<CommonResponse> registerWharfTimetable(HttpServletRequest request, @Valid @RequestBody RequestHarborTimetable.RegisterHarborTimetableDto requestDto) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }
        shipService.registerHarborTimetable(requestDto.getHarborName(), requestDto.getDestination(), requestDto.getPeriod(), requestDto.getOperatingTime());

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 시간 등록 성공")
                .build());
    }

    @PostMapping("/harbor/ship-fare")
    public ResponseEntity<CommonResponse> registerShipFare(HttpServletRequest request, @Valid @RequestBody RequestShipFare.RegisterShipFareDto registerShipFareDto) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }
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

    @GetMapping("/harbor/{id}/timetable/{destination}")
    public ResponseEntity<CommonResponse> getHarborTimetable(@PathVariable("id") Long id, @PathVariable("destination") String destination) {
        ResponseHarborTimetable.HarborTimetableDto list = shipService.getHarborTimetable(id, destination);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 시간 조회 성공")
                .list(list)
                .build());
    }

    @GetMapping("/harbor/{id}/ship-fare")
    public ResponseEntity<CommonResponse> getShipFare(@PathVariable("id") Long id) {
        ResponseShipFare.HarborShipFareDto responseHarborShipFare = shipService.getShipFare(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 시간 조회 성공")
                .list(responseHarborShipFare)
                .build());
    }

    @DeleteMapping("/harbor/ship-fare/{id}")
    public ResponseEntity<CommonResponse> deleteShipFare(HttpServletRequest request, @PathVariable("id") Long id) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }
        shipService.deleteShipFare(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 요금 삭제 성공")
                .build());
    }

    @DeleteMapping("/harbor/{id}")
    public ResponseEntity<CommonResponse> deleteHarbor(HttpServletRequest request, @PathVariable("id") Long id) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }
        shipService.deleteHarbor(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("항구 삭제 성공")
                .build());
    }

    @DeleteMapping("/harbor/timetable/{id}")
    public ResponseEntity<CommonResponse> deleteWharf(HttpServletRequest request, @PathVariable("id") Long id) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        if(!adminAuthenticationService.validAdminToken(token.orElseThrow(() -> new CustomJwtRuntimeException()))) {
            throw new CustomJwtRuntimeException();
        }
        shipService.deleteHarborTimetable(id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("배 시간 삭제 성공")
                .build());
    }
}
