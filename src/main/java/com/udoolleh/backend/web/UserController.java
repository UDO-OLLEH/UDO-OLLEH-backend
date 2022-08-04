package com.udoolleh.backend.web;

import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.exception.errors.LoginFailedException;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.UserService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/udo/user")
    public ResponseEntity<CommonResponse> requestRegister(@Valid @RequestBody RequestUser.Register registerDto) {

        userService.register(registerDto);

        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/udo/user/login")
    public ResponseEntity<CommonResponse> requestLogin(@Valid @RequestBody RequestUser.Login loginDto) {

        ResponseUser.Login manager = userService.login(loginDto).orElseThrow(() -> new LoginFailedException());

        HashMap<String, Object> map = new HashMap<>();
        map.put("accessToken", manager.getAccessToken());
        map.put("refreshToken", manager.getRefreshToken());

        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .list(map)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/udo/refreshToken")
    public ResponseEntity<CommonResponse> refreshToken(@RequestBody Map<String, String> payload) {
        ResponseUser.Token token = userService.refreshToken(payload.get("refreshToken")).orElseThrow(() -> new CustomJwtRuntimeException());

        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .list(token)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
