package com.udoolleh.backend.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.ProtocolException;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            //request method가 OPTIONS일 경우 JWT를 체크하지 않는 방향으로(개발 블로그 참고)
            return true;
        }
        return true;
        }
    }