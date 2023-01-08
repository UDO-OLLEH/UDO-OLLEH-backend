package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.AdminAuthenticationServiceInterface;
import com.udoolleh.backend.exception.CustomException;
import com.udoolleh.backend.exception.ErrorCode;
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
                .header("x-auth-token", token)
                .retrieve()
                .bodyToMono(CommonResponse.class)
                .doOnError(error -> {
                    System.out.println(error);
                    throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
                })
                .block();

        return true;
    }
}
