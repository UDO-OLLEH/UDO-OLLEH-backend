package com.udoolleh.backend.web;

import com.udoolleh.backend.exception.errors.LoginFailedException;
import com.udoolleh.backend.provider.service.UserService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final UserService userService;

    @GetMapping("/home")
    public String getHome(){
        return "Hello World!";
    }

    @GetMapping("/test")
    public ResponseEntity<CommonResponse> test(){
        ResponseUser.Token response = ResponseUser.Token.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
        CommonResponse commonResponse = CommonResponse.builder()
                .message("테스트 성공")
                .list(response)
                .build();
        return ResponseEntity.ok().body(commonResponse);
    }

    @PostMapping("/test/login")
    public ResponseEntity<CommonResponse> requestLogin(@Valid @RequestBody RequestUser.LoginDto loginDto) {

        ResponseUser.Token manager = userService.login(loginDto).orElseThrow(()->new LoginFailedException());

        CommonResponse commonResponse = CommonResponse.builder()
                .message("테스트 성공")
                .list(manager)
                .build();
        return ResponseEntity.ok().body(commonResponse);

    }
}
