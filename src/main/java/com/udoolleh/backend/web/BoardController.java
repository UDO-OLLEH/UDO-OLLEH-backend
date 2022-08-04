package com.udoolleh.backend.web;


import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.BoardService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestBoard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
public class BoardController {
    private final BoardService boardService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @PostMapping("/udo/board/registration")
    ResponseEntity<CommonResponse> registerPosts(HttpServletRequest request, @Valid @RequestBody RequestBoard.Creates requestDto) {
        boardService.registerPosts(requestDto);
        return new ResponseEntity<>(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("게시글 등록 성공")
                .build(), HttpStatus.OK);
    }
}
