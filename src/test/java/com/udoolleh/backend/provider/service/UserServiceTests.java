package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.UserNicknameDuplicatedException;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Test
    @DisplayName("회원가입 서비스 테스트")
    @Transactional
    void registerTest() {
        //given
        RequestUser.registerDto dto = RequestUser.registerDto.builder()
                .email("hello")
                .password("itsmypassword")
                .build();
        //when
        userService.register(dto);
        //then
        User user = userRepository.findByEmail(dto.getEmail());
        assertEquals(dto.getEmail(), user.getEmail());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
    }

    @Test
    @DisplayName("회원가입 테스트(실패 - 닉네임 중복됐을 경우)")
    @Transactional
    void registerTestWhenDuplicatedNickname() {
        //given
        RequestUser.registerDto dto = RequestUser.registerDto.builder()
                .email("hello")
                .nickname("nickname")
                .password("itsmypassword")
                .build();
        //when
        userService.register(dto);

        //닉네임 중복
        RequestUser.registerDto dto1 = RequestUser.registerDto.builder()
                .email("test")
                .nickname("nickname")
                .password("1234")
                .build();
        //then
        assertThrows(UserNicknameDuplicatedException.class, ()-> userService.register(dto1));
    }

    @Transactional
    @Test
    @DisplayName("로그인 서비스 테스트")
    void loginTest() {
        //given
        RequestUser.registerDto dto = RequestUser.registerDto.builder()
                .email("hello")
                .password("itsmypassword")
                .build();
        userService.register(dto);

        RequestUser.loginDto loginRequest = RequestUser.loginDto.builder()
                .email("hello")
                .password("itsmypassword")
                .build();

        //when
        ResponseUser.Login loginResponse = userService.login(loginRequest).orElseGet(()->null);
        System.out.println(loginResponse.getAccessToken());
        System.out.println(loginResponse.getRefreshToken());

        //then
        assertNotNull(loginResponse.getAccessToken());
        assertNotNull(loginResponse.getRefreshToken());
    }

    @Transactional
    @Test
    @DisplayName("토큰 갱신 테스트")
    void refreshTokenTest() {
        //given
        RequestUser.registerDto dto = RequestUser.registerDto.builder()
                .email("hello")
                .password("itsmypassword")
                .build();
        userService.register(dto);

        RequestUser.loginDto loginRequest = RequestUser.loginDto.builder()
                .email("hello")
                .password("itsmypassword")
                .build();

        //when
        ResponseUser.Login loginResponse = userService.login(loginRequest).orElseGet(()->null);
        ResponseUser.Token tokenResponse = userService.refreshToken(loginResponse.getRefreshToken()).orElseGet(()->null);
        //then
        assertNotNull(tokenResponse.getRefreshToken());
        assertNotNull(tokenResponse.getAccessToken());
        System.out.println(tokenResponse.getAccessToken());
        System.out.println(tokenResponse.getRefreshToken());
    }

}
