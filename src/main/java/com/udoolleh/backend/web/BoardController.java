package com.udoolleh.backend.web;


import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.LoginFailedException;
import com.udoolleh.backend.provider.security.JwtAuthToken;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.BoardService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestBoard;

import com.udoolleh.backend.web.dto.ResponseBoard;
import com.udoolleh.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @GetMapping("/udo/board/list")
    public ResponseEntity<CommonResponse> boardList(HttpServletRequest request, @PageableDefault Pageable pageable) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        Page<ResponseBoard.ListBoard> boards = boardService.boardList(email, pageable);
        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("게시글 조회 성공")
                .list(boards)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/udo/board")
    public ResponseEntity<CommonResponse> registerPosts(HttpServletRequest request,
                                                        @Valid @RequestBody RequestBoard.Register postDto) {

        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
            System.out.println(email);
        }
        boardService.registerPosts(email, postDto);
        CommonResponse response = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("게시글 등록 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/udo/board/{boardId}")
    public ResponseEntity<CommonResponse> modifyPosts(HttpServletRequest request, @PathVariable String boardId,
                                                      @Valid @RequestBody RequestBoard.Updates updatesDto) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
            System.out.println(email);
        }
        boardService.modifyPosts(email, boardId, updatesDto);

        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("게시글 수정 성공")
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/udo/board/{boardId}")
    public ResponseEntity<CommonResponse> deletePosts(HttpServletRequest request, @PathVariable String boardId) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
            System.out.println(email);
        }
        boardService.deletePosts(email, boardId);

        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("게시글 삭제 성공")
                .build(), HttpStatus.OK);
    }
}
