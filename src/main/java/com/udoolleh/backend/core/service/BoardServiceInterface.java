package com.udoolleh.backend.core.service;

import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.ResponseBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;

public interface BoardServiceInterface {
    //게시판 목록 조회
    Page<ResponseBoard.listBoardDto> boardList(String userEmail, Pageable pageable);

    //게시글 상세 조회
    ResponseBoard.detailBoardDto boardDetail(String userEmail, String id);

    //게시글 등록
    void registerPosts(MultipartFile file, String userEmail, RequestBoard.registerDto postDto);

    //게시글 수정
    void modifyPosts(MultipartFile file, String userEmail, String id, RequestBoard.updatesDto modifyDto);

    void deletePosts(String userEmail, String id);

    void updateVisit(String userEmail, String id);


}
