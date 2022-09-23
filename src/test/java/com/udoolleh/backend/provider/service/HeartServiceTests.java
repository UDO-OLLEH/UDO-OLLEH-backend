package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Heart;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.HeartRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class HeartServiceTests {
    @Autowired
    private HeartRepository heartRepository;
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
    private Heart heartEntity;


    @BeforeEach
    @DisplayName("좋아요 테스트 전 설정 항목")
    public void init() {
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

        userEntity = userRepository.findAll().get(0);
        List<Board> boards = boardRepository.findAll();

        for (Board entity : boards) {
            if (entity.getUser().equals(user) == false) {
                boardEntity = entity;
                break;
            }
        }
        heartEntity = Heart.builder()
                .board(boardEntity)
                .user(userEntity)
                .build();
    }

    @Test
    @DisplayName("좋아요 api 실행 - 버튼 눌렀을 때")
    void insertTest() {
        heartRepository.save(heartEntity);
        Heart heart = heartRepository.findById(heartEntity.getId()).get();
        assertEquals(heartEntity, heart);
    }

    @Test
    @DisplayName("좋아요 api 실행 - 버튼 두번 눌러서 취소하기")
    void deleteTest() {
        heartRepository.save(heartEntity);
        heartRepository.delete(heartEntity);
        Optional<Heart> deleteHeart = heartRepository.findById(heartEntity.getId());
        assertThat(deleteHeart.isPresent(), equalTo(false));
    }

    @Test
    @DisplayName("특정 게시물의 좋아요 카운트 조회하기")
    void countHeartsTest() {
        int countHeart = getCountHeart();

    }

    private int getCountHeart() {
        int max = (int) userRepository.count() - 1;
        return (int) (Math.random() * (max)) + 1;
    }

    private void insertHeart(Board newBoard, int countHeart) {
        boardRepository.save(newBoard);
        List<User> userEntities = userRepository.findAll().stream().filter(user -> user.equals(userEntity) == false).collect(Collectors.toList());
        for (int i = 0; i < countHeart; ++i) {
            heartRepository.save(Heart.builder()
                    .user(userEntities.get(i))
                    .board(newBoard)
                    .build());
        }
    }

}
