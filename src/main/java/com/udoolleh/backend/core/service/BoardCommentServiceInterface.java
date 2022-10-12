package com.udoolleh.backend.core.service;

import com.udoolleh.backend.web.dto.RequestBoardComment;
import com.udoolleh.backend.web.dto.ResponseBoardComment;

import java.util.List;

public interface BoardCommentServiceInterface {
    void registerBoardComment(String email, RequestBoardComment.RegisterBoardCommentDto registerDto);
    List<ResponseBoardComment.BoardCommentDto> getBoardComment(String email, String boardId);
    void modifyBoardComment(String email, RequestBoardComment.UpdateBoardCommentDto modifyDto);
    void deleteBoardComment(String email, String id);

}
