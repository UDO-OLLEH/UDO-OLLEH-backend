package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Likes;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.LikesRepository;
import com.udoolleh.backend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@SpringBootTest
@ActiveProfiles("test")
public class LikesServiceTests {
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

    private User userEntity;
    private Board boardEntity;
    private Likes likesEntity;

    @Transactional
    @BeforeEach
    public void init() {
        User user = User.builder()
                .email("k")
                .password("1234")
                .nickname("곽동현")
                .build();
        userEntity = userRepository.save(user);

        Board board = Board.builder()
                .title("테스트")
                .context("입니다.")
                .user(user)
                .build();
        boardEntity = boardRepository.save(board);

        likesEntity = Likes.builder()
                .board(board)
                .user(user)
                .build();

        likesRepository.save(likesEntity);
    }

    @Test
    @Transactional
    @DisplayName("게시글 좋아요 테스트 - 성공")
    void insertTest() {

        Likes likes = likesRepository.findById(likesEntity.getId()).get();

        assertEquals(likesEntity.getId(), likes.getId());

    }

    @Test
    @Transactional
    @DisplayName("게시글 좋아요 취소 테스트 - 성공")
    void deleteTest() {

        likesRepository.delete(likesEntity);
        Optional<Likes> deleteLikes = likesRepository.findById(likesEntity.getId());
        assertThat(deleteLikes.isPresent(), equalTo(false));
    }

    @Test
    @Transactional
    @DisplayName("게시글의 좋아요 개수 가져오기 - 성공")
    void likesCountTest() {
        int likesCount = getLikesCount();

        int findCount = likesRepository.countByBoard(boardEntity);

        assertThat(findCount, equalTo(likesCount));
        System.out.println(findCount);
    }

    private int getLikesCount() {
        int max = (int) boardRepository.count();
        return (int) (Math.random() * (max)) + 1;
    }

    private void insertLikesHistory(Board board, int likesCount) {

        List<User> userEntities = userRepository.findAll().stream()
                .filter(user -> user.equals(userEntity) == false)
                .collect(Collectors.toList());

        for (int i = 0; i < likesCount; ++i) {
            likesRepository.save(Likes.builder()
                    .user(userEntities.get(i))
                    .board(board)
                    .build());
        }
    }

    @Test
    @Transactional
    @DisplayName("사용자와 게시글을 이용해 좋아요 내역을 가져오기 - 성공")
    void findLikesTest() {
        User newUser = User.builder()
                .email("aliveJuicy")
                .password("1234")
                .nickname("새 테스트용")
                .build();
        //userRepository.findAll().stream().filter(user -> user.equals(userEntity) == false).findFirst().get();

        Board newBoard = Board.builder()
                .title("새 게시물")
                .context("입니다.")
                .user(newUser)
                .build();

        saveNewBoardAndGoodHistory(newUser, newBoard);

        Optional<Likes> savedEntity = likesRepository.findByUserAndBoard(newUser, newBoard);

        assertNotNull(savedEntity);
        assertThat(savedEntity.isPresent(), equalTo(Boolean.TRUE));

    }

    private void saveNewBoardAndGoodHistory(User newUser, Board newBoard) {
        userRepository.save(newUser);
        boardRepository.save(newBoard);
        likesRepository.save(Likes.builder()
                .user(newUser)
                .board(newBoard)
                .build());
    }
}



