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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @GetMapping("/board/list")
    public ResponseEntity<CommonResponse> boardList(HttpServletRequest request, @PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        Page<ResponseBoard.listBoardDto> listBoards = boardService.boardList(email, pageable);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시판 조회 성공")
                .list(listBoards)
                .build());
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<CommonResponse> boardDetail(HttpServletRequest request, @PathVariable String boardId) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        ResponseBoard.detailBoardDto detailBoards = boardService.boardDetail(email, boardId);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시판 상세 조회 성공")
                .list(detailBoards)
                .build());
    }

    @PostMapping("/board")
    public ResponseEntity<CommonResponse> registerPosts(HttpServletRequest request, @RequestPart(required = false) MultipartFile file,
                                                        @Valid @RequestPart RequestBoard.registerDto postDto) {

        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardService.registerPosts(file, email, postDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시판 등록 성공")
                .build());
    }

    @PutMapping("/board/{id}")
    public ResponseEntity<CommonResponse> modifyPosts(HttpServletRequest request, @RequestPart MultipartFile file, @PathVariable String boardId,
                                                      @Valid @RequestBody RequestBoard.updatesDto modifyDto) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardService.modifyPosts(file, email, boardId, modifyDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시판 수정 성공")
                .build());
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<CommonResponse> deletePosts(HttpServletRequest request, @PathVariable String boardId) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardService.deletePosts(email, boardId);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시판 삭제 성공")
                .build());
    }

    @GetMapping("/user/board")
    public ResponseEntity<CommonResponse> getMyBoardList(HttpServletRequest request, @PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
            Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
            String email = null;
            if (token.isPresent()) {
                JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
                email = jwtAuthToken.getData().getSubject();
            }
            Page<ResponseBoard.listBoardDto> myBoardList = boardService.getMyBoard(email,pageable);

            return ResponseEntity.ok().body(CommonResponse.builder()
                .message("내 게시물 조회 성공")
                .list(myBoardList)
                .build());
      }
    @GetMapping("/board/like")
    public ResponseEntity<CommonResponse> getLikeBoard(HttpServletRequest request, @PageableDefault Pageable pageable){
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
  
        Page<ResponseBoard.getLikeBoardDto> response = boardService.getLikeBoard(email, pageable);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("좋아요 한 게시판 조회 성공")
                .list(response)
                .build());
    }
}
