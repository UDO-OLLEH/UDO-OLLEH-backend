package com.udoolleh.backend.configuration;

import com.udoolleh.backend.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //List<String> excludePatterns = Arrays.asList("/**");
        registry.addInterceptor(authInterceptor) //Bean 등록
                .addPathPatterns("/api/login/**")
                .excludePathPatterns("/api/home/**");

    }
}
