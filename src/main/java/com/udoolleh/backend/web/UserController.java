package com.udoolleh.backend.web;

import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.exception.errors.LoginFailedException;
import com.udoolleh.backend.provider.security.JwtAuthToken;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.UserService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @PostMapping("/user")
    public ResponseEntity<CommonResponse> requestRegister(@Valid @RequestBody RequestUser.registerDto registerDto) {

        userService.register(registerDto);

        CommonResponse response = CommonResponse.builder()
                .message("성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> requestLogin(@Valid @RequestBody RequestUser.loginDto loginDto) {

        ResponseUser.Login manager = userService.login(loginDto).orElseThrow(() -> new LoginFailedException());

        HashMap<String, Object> map = new HashMap<>();
        map.put("accessToken", manager.getAccessToken());
        map.put("refreshToken", manager.getRefreshToken());

        CommonResponse response = CommonResponse.builder()
                .message("성공")
                .list(map)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletRequest request) {

        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }

        CommonResponse response = CommonResponse.builder()
                .message("로그아웃 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<CommonResponse> refreshToken(@RequestBody Map<String, String> payload) {
        ResponseUser.Token token = userService.refreshToken(payload.get("refreshToken")).orElseThrow(() -> new CustomJwtRuntimeException());

        CommonResponse response = CommonResponse.builder()
                .message("성공")
                .list(token)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/user")
    public ResponseEntity<CommonResponse> updateUser(HttpServletRequest request, @RequestPart(value = "file", required = false) MultipartFile file,
                                                     @RequestPart(value = "requestDto") RequestUser.updateDto updateDto){
        //유저 확인
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if(token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        userService.updateUser(email, file, updateDto);
        return new ResponseEntity<>(CommonResponse.builder()
                .message("유저 정보 수정 성공")
                .build(), HttpStatus.OK);

    }
}
