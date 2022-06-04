package com.udoolleh.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") //cors 문제 해결
                .allowCredentials(true);    //originPatterns를 와일드카드로 지정하고 credentials를 true로 주면 안됨(도메인 잡아줄것)
        //개발 블로그 참고
    }
}

