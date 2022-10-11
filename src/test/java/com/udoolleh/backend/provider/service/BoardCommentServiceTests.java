package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.BoardComment;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.NotFoundBoardException;
import com.udoolleh.backend.repository.BoardCommentRepository;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.LikeRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoardComment;
import com.udoolleh.backend.web.dto.ResponseBoardComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BoardCommentServiceTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardCommentRepository boardCommentRepository;
    @Autowired
    private BoardCommentService boardCommentService;

    @Test
    @Transactional
    @DisplayName("게시물 댓글 등록 테스트")
    void registerBoardCommentTest(){
        //given
        User user = User.builder()
                .email("test")
                .nickname("nickname")
                .password("password")
                .build();
        user = userRepository.save(user);

        Board board = Board.builder()
                .title("제목")
                .context("게시물 내용")
                .user(user)
                .build();
        board = boardRepository.save(board);

        RequestBoardComment.registerDto registerDto = RequestBoardComment.registerDto.builder()
                .boardId(board.getId())
                .context("댓글 내용")
                .build();
        //when
        boardCommentService.registerBoardComment("test", registerDto);

        //then
        board = boardRepository.findById(board.getId()).orElseThrow(() -> new NotFoundBoardException());

        assertEquals(1, board.getBoardComments().size());
        assertNotNull(boardCommentRepository.findByContext("댓글 내용"));
    }

    @Test
    @Transactional
    @DisplayName("게시물 댓글 조회 테스트")
    void getBoardCommentTest(){
        //given
        User user = User.builder()
                .email("test")
                .nickname("nickname")
                .password("password")
                .build();
        user = userRepository.save(user);

        Board board = Board.builder()
                .title("제목")
                .context("게시물 내용")
                .user(user)
                .build();
        board = boardRepository.save(board);

        BoardComment boardComment = BoardComment.builder()
                .user(user)
                .board(board)
                .context("댓글 내용")
                .build();
        board.addBoardComment(boardCommentRepository.save(boardComment));

        //then
        List<ResponseBoardComment.boardCommentDto> boardComments = boardCommentService.getBoardComment("test",board.getId());

        assertEquals(boardComments.size() , 1);
        assertEquals(boardComments.get(0).getNickname(), "nickname");
    }
}
