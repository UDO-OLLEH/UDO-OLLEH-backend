package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.BoardComment;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.CustomException;
import com.udoolleh.backend.exception.ErrorCode;
import com.udoolleh.backend.repository.BoardCommentRepository;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoardComment;
import com.udoolleh.backend.web.dto.ResponseBoardComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        RequestBoardComment.RegisterBoardCommentDto registerDto = RequestBoardComment.RegisterBoardCommentDto.builder()
                .boardId(board.getId())
                .context("댓글 내용")
                .build();
        //when
        boardCommentService.registerBoardComment("test", registerDto);

        //then
        board = boardRepository.findById(board.getId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOARD));

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
        List<ResponseBoardComment.BoardCommentDto> boardComments = boardCommentService.getBoardComment("test",board.getId());

        assertEquals(boardComments.size() , 1);
        assertEquals(boardComments.get(0).getNickname(), "nickname");
    }
    @Test
    @Transactional
    @DisplayName("게시물 댓글 수정 테스트")
    void modifyBoardCommentTest() throws InterruptedException {
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

        boardComment = boardCommentRepository.findByContext("댓글 내용");

        LocalDateTime beforeModificationTime = boardComment.getCreateAt();
        RequestBoardComment.UpdateBoardCommentDto modifyDto = RequestBoardComment.UpdateBoardCommentDto.builder()
                .commentId(boardComment.getId())
                .context("댓글 내용 변경")
                .build();
        //when
        Thread.sleep(3000);
        boardCommentService.modifyBoardComment("test", modifyDto);
        //then
        BoardComment modifiedBoardComment= boardCommentRepository.findByContext("댓글 내용 변경");
        assertEquals(board.getBoardComments().get(0).getContext(), boardComment.getContext());
        assertEquals(boardComment.getId(), modifiedBoardComment.getId());
        assertNotEquals(beforeModificationTime, modifiedBoardComment.getCreateAt());
    }
    @Test
    @Transactional
    @DisplayName("게시물 댓글 삭제 테스트")
    void deleteBoardCommentTest() {
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
        boardComment = boardCommentRepository.save(boardComment);
        board.addBoardComment(boardComment);

        assertNotNull(boardCommentRepository.findAll());
        //when
        boardCommentService.deleteBoardComment("test", boardComment.getId());
        //then
        assertEquals(0, boardCommentRepository.findAll().size());
        assertEquals(0, boardRepository.findAll().get(0).getBoardComments().size());
    }
}
