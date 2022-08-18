package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestMenu;
import com.udoolleh.backend.web.dto.ResponseMenuDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuServiceInterface {
    void registerMenu(MultipartFile file, RequestMenu.registerDto requestDto);
    List<ResponseMenuDto.getMenu> getMenu(String restaurantId);
    void deleteMenu(String restaurantId, String menuName);
}
