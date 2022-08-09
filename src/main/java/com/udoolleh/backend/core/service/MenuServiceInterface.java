package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestMenuDto;
import org.springframework.web.multipart.MultipartFile;

public interface MenuServiceInterface {
    void registerMenu(MultipartFile file, RequestMenuDto.register requestDto);;
}
