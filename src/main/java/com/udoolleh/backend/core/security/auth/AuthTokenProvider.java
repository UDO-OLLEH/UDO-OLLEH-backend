package com.udoolleh.backend.core.security.auth;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

public interface AuthTokenProvider<T> {
    T createAuthToken(String id, String role, Date expiredDate);
    T convertAuthToken(String token);

    Optional<String> resolveToken(HttpServletRequest request);
}
