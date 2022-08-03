package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.ResponseBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardServiceInterface {
    //게시판 목록 조회
    Page<ResponseBoard> getBoardPage();
    //게시글 상세 조회
    Optional<ResponseBoard> getPostsPage(Long id);
    //게시글 등록
    void registerPosts(RequestBoard.Creates createsDto);
    //게시글 수정
    void modifyPosts(RequestBoard.Updates updatesDto);
}
