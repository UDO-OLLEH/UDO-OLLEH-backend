package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.RequestCourse;
import com.udoolleh.backend.web.dto.ResponseBoard;
import com.udoolleh.backend.web.dto.ResponseCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseServiceInterface {
  void registerCourse(RequestCourse.RegisterDto RequestDto);
  List<ResponseCourse.GetDto> getCourses();
  void deleteCourse(Long id);
  }
