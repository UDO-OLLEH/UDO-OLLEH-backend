package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestBoardComment;
import com.udoolleh.backend.web.dto.ResponseBoardComment;

import java.util.List;

public interface BoardCommentServiceInterface {
    void registerBoardComment(String email, RequestBoardComment.registerDto registerDto);
    List<ResponseBoardComment.boardCommentDto> getBoardComment(String email, String boardId);
    void modifyBoardComment(String email, RequestBoardComment.modifyDto modifyDto);
    void deleteBoardComment(String email, String id);

}
