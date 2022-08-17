package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.entity.Review;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.RequestReviewDto;
import com.udoolleh.backend.web.dto.RequestUser;
import com.udoolleh.backend.web.dto.ResponseBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class BoardServiceTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @DisplayName("게시글 조회 테스트")
    void boardListTest() {
        User user = User.builder()
                .email("k")
                .password("1234")
                .build();
        user = userRepository.save(user);

        RequestBoard.Register dto = RequestBoard.Register.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerPosts("k", dto);

        RequestBoard.Register dto2 = RequestBoard.Register.builder()
                .title("아")
                .context("우아")
                .build();
        boardService.registerPosts("k", dto2);


        Pageable pageable = PageRequest.of(0, 2);

        Page<ResponseBoard.ListBoard> list = boardService.boardList(user.getEmail(), pageable);
        assertNotNull(list);
        for (ResponseBoard.ListBoard listBoard : list) {
            System.out.println(listBoard.getTitle() + listBoard.getContext() + listBoard.getCreateAt());
        }

    }

    @Test
    @Transactional
    @DisplayName("게시글 등록 테스트")
    void registerPostsTest() {
        User user = User.builder()
                .email("k")
                .password("1234")
                .build();
        user = userRepository.save(user);

        RequestBoard.Register dto = RequestBoard.Register.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerPosts("k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());

        assertEquals(board.getTitle(), dto.getTitle());
        assertNotNull(user.getBoardList());

    }

    @Test
    @Transactional
    @DisplayName("게시글 수정 테스트")
    void modifyPostsTest() {
        User user = User.builder()
                .email("k")
                .password("1234")
                .build();
        user = userRepository.save(user);

        RequestBoard.Register dto = RequestBoard.Register.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerPosts("k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());

        RequestBoard.Updates mDto = RequestBoard.Updates.builder()
                .title("수정한 제목")
                .context("게시글 수정 내용")
                .build();

        boardService.modifyPosts("k", board.getBoardId(), mDto);

        board = boardRepository.findByTitleAndContext(mDto.getTitle(), mDto.getContext());

        assertTrue(board.getTitle().equals("수정한 제목"));
        System.out.println(board.getTitle());
        System.out.println(user.getEmail());

    }

    @Test
    @Transactional
    @DisplayName("게시글 삭제 테스트")
    void deleteReviewTest() {
        User user = User.builder()
                .email("k")
                .password("1234")
                .build();
        user = userRepository.save(user);


        RequestBoard.Register dto = RequestBoard.Register.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerPosts("k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());


        boardService.deletePosts("k", board.getBoardId());

        assertNull(boardRepository.findByUserAndBoardId(user, board.getBoardId()));
        assertFalse(user.getBoardList().contains(board));
    }

}
