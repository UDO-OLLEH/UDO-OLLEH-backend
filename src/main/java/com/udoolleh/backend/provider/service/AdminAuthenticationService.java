package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.AdminAuthenticationServiceInterface;
import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.web.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AdminAuthenticationService implements AdminAuthenticationServiceInterface {
    private final WebClient udoOllehMicroserviceWebClient;

    @Override
    public boolean validAdminToken(String token) {
        System.out.println("validAdmin탐");
        //토큰 검증
        CommonResponse result = udoOllehMicroserviceWebClient.get()
                .uri("/admin/valid/accessToken")
                .header("x-auth-token",token)
                .retrieve()
                .bodyToMono(CommonResponse.class)
                .doOnError(error -> {
                    System.out.println(error);
                    throw new CustomJwtRuntimeException();
                })
                .block();

        System.out.println("webclient끝");

        return true;
    }
}
