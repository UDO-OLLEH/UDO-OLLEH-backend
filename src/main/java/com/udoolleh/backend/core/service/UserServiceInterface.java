package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;

import java.util.Optional;

public interface UserServiceInterface {
    void register(RequestUser.Register registerDto);
    Optional<ResponseUser.Login> login(RequestUser.Login loginDto);
    Optional<ResponseUser.Token> refreshToken(String token);
    String createAccessToken(String id);
    String createRefreshToken(String id);
}
