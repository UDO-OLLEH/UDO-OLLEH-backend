package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.service.ShipService;
import com.udoolleh.backend.web.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShipController {
    private final ShipService shipService;

    @PostMapping("/udo/wharf")
    public ResponseEntity<CommonResponse> addWharf(@RequestBody Map<String, String> wharf){
        shipService.registerWharf(wharf.get("wharf"));

        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("선착장 등록 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
