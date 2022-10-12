package com.udoolleh.backend.provider.service;

import com.amazonaws.Response;
import com.udoolleh.backend.core.service.BoardCommentServiceInterface;
import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.BoardComment;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.exception.errors.NotFoundBoardCommentException;
import com.udoolleh.backend.exception.errors.NotFoundBoardException;
import com.udoolleh.backend.exception.errors.NotFoundRestaurantException;
import com.udoolleh.backend.repository.BoardCommentRepository;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoardComment;
import com.udoolleh.backend.web.dto.ResponseBoardComment;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
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
    public void registerBoardComment(String email, RequestBoardComment.RegisterBoardCommentDto registerDto){
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
    @Transactional(readOnly = true)
    public List<ResponseBoardComment.BoardCommentDto> getBoardComment(String email, String boardId){
        Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(() -> new CustomJwtRuntimeException());
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundBoardException());

        List<BoardComment> boardComments = boardCommentRepository.findByBoard(board);
        List<ResponseBoardComment.BoardCommentDto> responseDto = new ArrayList<>();

        for(BoardComment boardComment : boardComments){
            ResponseBoardComment.BoardCommentDto item = ResponseBoardComment.BoardCommentDto.builder()
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

    /**
     * @param email
     * @param modifyDto
     * 댓글 수정 - 댓글 수정시 댓글의 createAt도 새로 변경됨
     */
    @Override
    @Transactional
    public void modifyBoardComment(String email, RequestBoardComment.UpdateBoardCommentDto modifyDto) {
        User user = Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(()->new CustomJwtRuntimeException());
        BoardComment boardComment = boardCommentRepository.findById(modifyDto.getCommentId()).orElseThrow(()->new NotFoundBoardCommentException());
        if(!email.equals(boardComment.getUser().getEmail())){
            throw new CustomJwtRuntimeException();  //같은 사용자가 아니면 예외 던짐
        }
        boardComment.updateContext(modifyDto.getContext());
    }

    @Override
    @Transactional
    public void deleteBoardComment(String email, String id) {
        User user = Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(()->new CustomJwtRuntimeException());
        BoardComment boardComment = boardCommentRepository.findById(id).orElseThrow(()->new NotFoundBoardCommentException());
        if(!email.equals(boardComment.getUser().getEmail())){
            throw new CustomJwtRuntimeException();  //같은 사용자가 아니면 예외 던짐
        }
        Board board = boardRepository.findById(boardComment.getBoard().getId()).orElseThrow(() -> new NotFoundBoardException());
        board.getBoardComments().remove(boardComment);
    }
}
