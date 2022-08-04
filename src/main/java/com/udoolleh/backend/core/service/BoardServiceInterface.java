package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.ResponseBoard;


import java.util.List;
import java.util.Optional;

public interface BoardServiceInterface {
    //게시판 목록 조회

    //게시글 상세 조회

    //게시글 등록
    void registerPosts(RequestBoard.Creates requestDto);
    //게시글 수정
    void modifyPosts(RequestBoard.Updates requestDto);
}
