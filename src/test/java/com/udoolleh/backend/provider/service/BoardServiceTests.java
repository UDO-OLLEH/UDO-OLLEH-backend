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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ActiveProfiles("test")
public class BoardServiceTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;

    @Test
    @Transactional
    @DisplayName("게시글 등록 테스트")
    void registerPostsTest() {
        User user = User.builder()
                .email("alive")
                .password("1234")
                .build();
        user = userRepository.save(user);

        RequestBoard.Creates requestDto = RequestBoard.Creates.builder()
                .user(user)
                .title("테스트입니다")
                .content("잘 되나요?")
                .build();

        boardService.registerPosts(requestDto);
        //ResponseBoard.Board boardResponse
        //boardService.registerPosts(requestDto);
        assertNotNull(requestDto.getTitle());
        assertNotNull(requestDto.getContent());

        //assertEquals("테스트입니다", requestDto.getTitle());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        System.out.println(requestDto.getTitle());
        System.out.println(requestDto.getContent());
        //System.out.println(.getCreatedDate());

    }
}
