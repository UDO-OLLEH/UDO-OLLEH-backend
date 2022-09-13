package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.ResponseBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
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

        RequestBoard.registerDto dto = RequestBoard.registerDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerPosts(null, "k", dto);

        RequestBoard.registerDto dto2 = RequestBoard.registerDto.builder()
                .title("아")
                .context("우아")
                .build();
        boardService.registerPosts(null, "k", dto2);


        Pageable pageable = PageRequest.of(0, 10);

        Page<ResponseBoard.listBoardDto> list = boardService.boardList(user.getEmail(), pageable);
        assertNotNull(list);
        for (ResponseBoard.listBoardDto listBoard : list) {
            System.out.println(listBoard.getTitle() + listBoard.getContext() + listBoard.getCreateAt());
        }

    }

    @Test
    @Transactional
    @DisplayName("게시글 등록 테스트(사진 파일 있을 때) - 성공")
    void registerPostsTestWhenExistPhoto() {
        User user = User.builder()
                .email("k")
                .password("1234")
                .build();
        user = userRepository.save(user);

        RequestBoard.registerDto dto = RequestBoard.registerDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.png",
                "image/png", "test data".getBytes());

        boardService.registerPosts(mockMultipartFile, "k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());

        assertEquals(board.getTitle(), dto.getTitle());
        assertNotNull(user.getBoardList());
        assertNotNull(board.getPhoto());

    }

    @Test
    @Transactional
    @DisplayName("게시글 등록 테스트(사진 파일 없을 때) - 성공")
    void registerPostsTestWhenNotExistPhoto() {
        User user = User.builder()
                .email("k")
                .password("1234")
                .build();
        user = userRepository.save(user);

        RequestBoard.registerDto dto = RequestBoard.registerDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerPosts(null, "k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());

        assertEquals(board.getTitle(), dto.getTitle());
        assertNotNull(user.getBoardList());

    }

    @Test
    @Transactional
    @DisplayName("게시글 수정 테스트(사진 추가) - 성공")
    void modifyPostsTest() {
        User user = User.builder()
                .email("k")
                .password("1234")
                .build();
        user = userRepository.save(user);

        RequestBoard.registerDto dto = RequestBoard.registerDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerPosts(null, "k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());

        RequestBoard.updatesDto mDto = RequestBoard.updatesDto.builder()
                .title("수정한 제목")
                .context("게시글 수정 내용")
                .build();
        boardService.modifyPosts(null, "k", board.getId(), mDto);

        board = boardRepository.findByTitleAndContext(mDto.getTitle(), mDto.getContext());

        assertTrue(board.getTitle().equals("수정한 제목"));
        System.out.println(board.getTitle());
        System.out.println(user.getEmail());

    }

    @Test
    @Transactional
    @DisplayName("게시글 수정 테스트(사진 추가) - 성공")
    void modifyPostsTestWhenAddPhoto() {
        User user = User.builder()
                .email("k")
                .password("1234")
                .build();
        user = userRepository.save(user);

        RequestBoard.registerDto dto = RequestBoard.registerDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerPosts(null, "k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());

        RequestBoard.updatesDto mDto = RequestBoard.updatesDto.builder()
                .title("수정한 제목")
                .context("게시글 수정 내용")
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.png",
                "image/png", "test data".getBytes());

        boardService.modifyPosts(mockMultipartFile, "k", board.getId(), mDto);

        board = boardRepository.findByTitleAndContext(mDto.getTitle(), mDto.getContext());

        assertTrue(board.getTitle().equals("수정한 제목"));
        assertNotNull(board.getPhoto());
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


        RequestBoard.registerDto dto = RequestBoard.registerDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerPosts(null, "k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());


        boardService.deletePosts("k", board.getId());

        assertNull(boardRepository.findByUserAndId(user, board.getId()));
        assertFalse(user.getBoardList().contains(board));
    }

}
