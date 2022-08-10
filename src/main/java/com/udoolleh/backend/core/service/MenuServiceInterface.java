package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestMenuDto;
import com.udoolleh.backend.web.dto.ResponseMenuDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuServiceInterface {
    void registerMenu(MultipartFile file, RequestMenuDto.register requestDto);
    List<ResponseMenuDto.getMenu> getMenu(String restaurantId);
}
