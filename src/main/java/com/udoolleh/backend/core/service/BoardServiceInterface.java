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
    Page<ResponseBoard.BoardListDto> getBoardList(String userEmail, Pageable pageable);

    //게시글 상세 조회
    ResponseBoard.BoardDto getBoardDetail(String userEmail, String id);

    //게시글 등록
    void registerBoard(MultipartFile file, String userEmail, RequestBoard.RegisterBoardDto postDto);

    //게시글 수정
    void updateBoard(MultipartFile file, String userEmail, String id, RequestBoard.UpdateBoardDto modifyDto);

    void deleteBoard(String userEmail, String id);

    void updateVisit(String userEmail, String id);

    void updateLikes(String userEmail, String id);

    void deleteLikes(String userEmail, String likedId, String id);

    Page<ResponseBoard.BoardListDto> getMyBoard(String email, Pageable pageable);
    
    //좋아요 누른 게시글 조회
    Page<ResponseBoard.LikeBoardDto> getLikeBoard(String email, Pageable pageable);

}
