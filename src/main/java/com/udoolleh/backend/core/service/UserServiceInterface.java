package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserServiceInterface {
    void register(RequestUser.registerDto registerDto);
    Optional<ResponseUser.Login> login(RequestUser.loginDto loginDto);
    Optional<ResponseUser.Token> refreshToken(String token);
    String createAccessToken(String id);
    String createRefreshToken(String id);
    void logout(String email);
    void updateUser(String email, MultipartFile file, RequestUser.updateDto updateDto);
}
