package com.udoolleh.backend.provider.service;

import com.amazonaws.Response;
import com.udoolleh.backend.core.service.BoardCommentServiceInterface;
import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.BoardComment;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.exception.errors.NotFoundBoardException;
import com.udoolleh.backend.exception.errors.NotFoundRestaurantException;
import com.udoolleh.backend.repository.BoardCommentRepository;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoardComment;
import com.udoolleh.backend.web.dto.ResponseBoardComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardCommentService implements BoardCommentServiceInterface {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;

    @Override
    @Transactional
    public void registerBoardComment(String email, RequestBoardComment.registerDto registerDto){
        User user = Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(() -> new CustomJwtRuntimeException());
        Board board = boardRepository.findById(registerDto.getBoardId()).orElseThrow(() -> new NotFoundRestaurantException());

        BoardComment boardComment = BoardComment.builder()
                .context(registerDto.getContext())
                .board(board)
                .user(user)
                .build();
        board.addBoardComment(boardCommentRepository.save(boardComment));
    }

    @Override
    @Transactional
    public List<ResponseBoardComment.boardCommentDto> getBoardComment(String email, String boardId){
        Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(() -> new CustomJwtRuntimeException());
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundBoardException());

        List<BoardComment> boardComments = boardCommentRepository.findByBoard(board);
        List<ResponseBoardComment.boardCommentDto> responseDto = new ArrayList<>();

        for(BoardComment boardComment : boardComments){
            ResponseBoardComment.boardCommentDto item = ResponseBoardComment.boardCommentDto.builder()
                    .id(boardComment.getId())
                    .createAt(boardComment.getCreateAt())
                    .nickname(boardComment.getUser().getNickname())
                    .photo(boardComment.getUser().getProfile())
                    .context(boardComment.getContext())
                    .build();
            responseDto.add(item);
        }
        return responseDto;
    }

}
