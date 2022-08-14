package com.udoolleh.backend.provider.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@PropertySource("classpath:/secrets/kakao-api-secrets.properties")
@SpringBootTest
@ActiveProfiles("test")
public class RestaurantServiceTests {
}
