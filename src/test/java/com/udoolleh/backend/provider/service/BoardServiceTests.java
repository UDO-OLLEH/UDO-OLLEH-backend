package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Likes;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.LikeRepository;
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
    private LikeRepository likeRepository;
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

        RequestBoard.RegisterBoardDto dto = RequestBoard.RegisterBoardDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerBoard(null, "k", dto);

        RequestBoard.RegisterBoardDto dto2 = RequestBoard.RegisterBoardDto.builder()
                .title("아")
                .context("우아")
                .build();
        boardService.registerBoard(null, "k", dto2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ResponseBoard.BoardListDto> list = boardService.getBoardList(user.getEmail(), pageable);
        assertNotNull(list);
        for (ResponseBoard.BoardListDto listBoard : list) {
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

        RequestBoard.RegisterBoardDto dto = RequestBoard.RegisterBoardDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test1.png",
                "image/png", "test data".getBytes());

        boardService.registerBoard(mockMultipartFile, "k", dto);

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

        RequestBoard.RegisterBoardDto dto = RequestBoard.RegisterBoardDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerBoard(null, "k", dto);

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

        RequestBoard.RegisterBoardDto dto = RequestBoard.RegisterBoardDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerBoard(null, "k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());

        RequestBoard.UpdateBoardDto mDto = RequestBoard.UpdateBoardDto.builder()
                .title("수정한 제목")
                .context("게시글 수정 내용")
                .build();
        boardService.updateBoard(null, "k", board.getId(), mDto);

        board = boardRepository.findByTitleAndContext(mDto.getTitle(), mDto.getContext());

        assertEquals("수정한 제목", board.getTitle());
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

        RequestBoard.RegisterBoardDto dto = RequestBoard.RegisterBoardDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerBoard(null, "k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());

        RequestBoard.UpdateBoardDto mDto = RequestBoard.UpdateBoardDto.builder()
                .title("수정한 제목")
                .context("게시글 수정 내용")
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test1.png",
                "image/png", "test data".getBytes());

        boardService.updateBoard(mockMultipartFile, "k", board.getId(), mDto);

        board = boardRepository.findByTitleAndContext(mDto.getTitle(), mDto.getContext());

        assertEquals("수정한 제목", board.getTitle());
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


        RequestBoard.RegisterBoardDto dto = RequestBoard.RegisterBoardDto.builder()
                .title("지금 몇시냐")
                .context("잘 자")
                .build();
        boardService.registerBoard(null, "k", dto);

        Board board = boardRepository.findByTitleAndContext(dto.getTitle(), dto.getContext());


        boardService.deleteBoard("k", board.getId());

        assertNull(boardRepository.findByUserAndId(user, board.getId()));
        assertFalse(user.getBoardList().contains(board));
    }
    @Test
    @DisplayName("자신의 게시물 조회 테스트")
    void getMyBoardListTest() {
        //given
        User user = User.builder()
                .email("him1")
                .password("1234")
                .build();
        user = userRepository.save(user);
        User otherUser = User.builder()
                .email("him2")
                .password("9999")
                .build();
        userRepository.save(otherUser);

        Board board1 = Board.builder()
                .user(user)
                .title("제목입니다1")
                .context("내용입니다1")
                .build();
        boardRepository.save(board1);

        Board board2 = Board.builder()
                .user(user)
                .title("제목입니다2")
                .context("내용입니다2")
                .build();
        boardRepository.save(board2);

        Board board3 = Board.builder()
                .title("다른 사용자의 게시물")
                .context("내용입니다3")
                .build();
        boardRepository.save(board3);

        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<ResponseBoard.BoardListDto> list = boardService.getMyBoard(user.getEmail(), pageable);
        //then
        assertEquals(2 ,list.toList().size());
    }
    @Test
    @DisplayName("좋아요 한 게시판 조회 테스트(성공)")
    void getLikeBoardTest(){
        User user = User.builder()
                .email("test")
                .nickname("nick")
                .password("1234")
                .build();
        user = userRepository.save(user);

        Board board = Board.builder()
                .title("제목")
                .context("내용")
                .user(user)
                .build();
        board = boardRepository.save(board);


        Board board1 = Board.builder()
                .title("제목1")
                .context("내용1")
                .user(user)
                .build();
        board1 = boardRepository.save(board1);

        //좋아요
        Likes like = Likes.builder()
                .board(board)
                .user(user)
                .build();
        like = likeRepository.save(like);


        //좋아요
        Likes like1 = Likes.builder()
                .board(board1)
                .user(user)
                .build();
        like1 = likeRepository.save(like1);

        //페이지 크기 설정
        Pageable pageable = PageRequest.of(0, 3);
        Page<ResponseBoard.LikeBoardDto> response =  boardService.getLikeBoard("test", pageable);
        assertNotNull(response);
        assertEquals(2, response.getTotalElements());
    }
}
