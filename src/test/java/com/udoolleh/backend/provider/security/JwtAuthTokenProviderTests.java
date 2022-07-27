package com.udoolleh.backend.provider.security;

import com.udoolleh.backend.core.security.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("test")
public class JwtAuthTokenProviderTests {
    @Autowired
    JwtAuthTokenProvider jwtAuthTokenProvider;

    @DisplayName("jwt 토큰 발급 테스트")
    @Test
    void createTokenTest(){
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(2).atZone(ZoneId.systemDefault()).toInstant());
        JwtAuthToken accessToken = jwtAuthTokenProvider.createAuthToken("helloId", Role.ADMIN.getCode(),expiredDate);
        System.out.println(accessToken.getToken());
    }
}
