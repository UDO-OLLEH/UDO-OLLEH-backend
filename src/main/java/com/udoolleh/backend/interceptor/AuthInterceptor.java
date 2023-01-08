package com.udoolleh.backend.interceptor;

import com.udoolleh.backend.exception.CustomException;
import com.udoolleh.backend.exception.ErrorCode;
import com.udoolleh.backend.provider.security.JwtAuthToken;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler)
            throws Exception {

        log.info("preHandle!");
        if (servletRequest.getMethod().equals("OPTIONS")) {
            return true;
        }

        Optional<String> token = jwtAuthTokenProvider.resolveToken(servletRequest);
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            if (jwtAuthToken.validate()) {
                return true;
            } else {
                throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
            }
        } else {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }

    }

}



