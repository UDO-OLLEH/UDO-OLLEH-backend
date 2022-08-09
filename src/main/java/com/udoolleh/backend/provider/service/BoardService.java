package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.BoardServiceInterface;
import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Review;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.exception.errors.NotFoundBoardException;
import com.udoolleh.backend.exception.errors.NotFoundReviewException;
import com.udoolleh.backend.exception.errors.NotFoundUserException;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService implements BoardServiceInterface {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //게시글 등록 API
    @Override
    @Transactional
    public void registerPosts(String email, RequestBoard.Register postDto) { //postDto = title, context + category 추후 추가
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }

        //Board board = boardRepository.findByTitleContext(postDto.getTitle(), postDto.getContext);
        //if (board == null) { ~ }
        //빌더 패턴으로 게시글 작성
        Board board = Board.builder()
                .title(postDto.getTitle())
                .context(postDto.getContext())
                .user(user)
                .build();
        //게시글을 등록하고 저장
        boardRepository.save(board);
        user.addBoard(board);
    }

    @Override
    @Transactional
    public void modifyPosts(String email, Long boardId, RequestBoard.Updates updatesDto) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findByUserAndBoardId(user, boardId);
        if (board == null) {
            throw new NotFoundBoardException();
        }
        board.modifyPosts(updatesDto.getTitle(), updatesDto.getContext());
    }
    @Override
    @Transactional
    public void deletePosts(String email, Long boardId){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findByUserAndBoardId(user, boardId);
        if(board == null){
            throw new NotFoundBoardException();
        }

        //board.getReply().getBoardList().remove(board); 식당 <- 리뷰 / 게시판 <- 댓글 1:N

        user.getBoardList().remove(board);
        boardRepository.deleteById(boardId);
    }

}
