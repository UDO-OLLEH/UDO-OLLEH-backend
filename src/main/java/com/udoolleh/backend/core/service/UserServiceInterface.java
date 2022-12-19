package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserServiceInterface {
    void register(RequestUser.RegisterUserDto registerDto);
    Optional<ResponseUser.Token> login(RequestUser.LoginDto loginDto);
    Optional<ResponseUser.Token> refreshToken(String token);
    String createAccessToken(String id);
    String createRefreshToken(String id);
    void logout(String email);
    void updateUser(String email, RequestUser.UpdateUserDto requestDto);
    void uploadUserImage(String email, MultipartFile image);
}
