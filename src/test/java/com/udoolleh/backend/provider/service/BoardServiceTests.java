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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    @DisplayName("게시글 등록 테스트")
    void registerPostsTest() {

        RequestUser.Register registerDto = RequestUser.Register.builder()
                .email("k")
                .password("1234")
                .build();
        userService.register(registerDto); //회원 가입

        RequestBoard.Register dto = RequestBoard.Register.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build(); //게시글 등록
        boardService.registerPosts("k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());

        assertNotNull(dto);
        assertEquals(board.getTitle(), dto.getTitle());
        assertEquals(board.getContext(), dto.getContext());

        System.out.println(board.getTitle());
        System.out.println(board.getContext());
        System.out.println(board.getUser().getEmail());
        System.out.println(board.getUser().getId());
    }

    @Test
    @Transactional
    @DisplayName("게시글 수정 테스트(성공)")
    void modifyPostsTest() {
        User user = User.builder()
                .email("test")
                .password("1234")
                .build();
        userRepository.save(user);

        RequestBoard.Register dto = RequestBoard.Register.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build(); //게시글 등록
        boardService.registerPosts("test", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());
        //리뷰 수정
        RequestBoard.Updates mDto = RequestBoard.Updates.builder()
                .title("수정한 제목")
                .context("게시글 수정 내용")
                .build();

        boardService.modifyPosts("test", board.getBoardId(), mDto);

        Board result = boardRepository.findByTitleAndContext(mDto.getTitle(), mDto.getContext());

        assertTrue(result.getTitle().equals("수정한 제목"));

        System.out.println(result.getTitle());
        System.out.println(result.getContext());
    }
}
