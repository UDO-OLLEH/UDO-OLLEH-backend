package com.udoolleh.backend.interceptor;

import com.udoolleh.backend.core.security.role.Role;
import com.udoolleh.backend.exception.CustomAuthenticationException;
import com.udoolleh.backend.provider.security.JwtAuthToken;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private static final String AUTHORIZATION_HEADER = "X-auth-token";

    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler)
            throws Exception{

        log.info("preHandle");

        Optional<String> token = resolveToken(servletRequest);

        if(token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            if (jwtAuthToken.validate() & Role.USER.getCode().equals(jwtAuthToken.getData().get("role"))) {
                return true;
            } else {
                throw new CustomAuthenticationException();
            }
        }else{
            throw new CustomAuthenticationException();
        }

    }

    private Optional<String> resolveToken(HttpServletRequest request){
        String authToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(authToken)){
            return Optional.of(authToken);
        }else {
            return Optional.empty();
        }
    }
}


