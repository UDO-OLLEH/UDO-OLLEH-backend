package com.udoolleh.backend.web;


import com.udoolleh.backend.provider.security.JwtAuthToken;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.BoardService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestBoard;

import com.udoolleh.backend.web.dto.ResponseBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @GetMapping("/board/list")
    public ResponseEntity<CommonResponse> getBoardList(HttpServletRequest request,
                                                    @PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        Page<ResponseBoard.BoardListDto> boardList = boardService.getBoardList(email, pageable);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시판 조회 성공")
                .list(boardList)
                .build());
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<CommonResponse> getBoardDetail(HttpServletRequest request, @PathVariable String id) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        ResponseBoard.BoardDto detailBoards = boardService.getBoardDetail(email, id);
        boardService.updateVisit(email, id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시판 상세 조회 성공")
                .list(detailBoards)
                .build());
    }

    @PostMapping("/board")
    public ResponseEntity<CommonResponse> registerBoard(HttpServletRequest request, @RequestPart(required = false) MultipartFile file,
                                                        @Valid @RequestPart RequestBoard.RegisterBoardDto requestDto) {

        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardService.registerBoard(file, email, requestDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시판 등록 성공")
                .build());
    }

    @PostMapping("/board/{id}")
    public ResponseEntity<CommonResponse> updateBoard(HttpServletRequest request, @RequestPart MultipartFile file, @PathVariable String id,
                                                      @Valid @RequestPart RequestBoard.UpdateBoardDto updateBoardDto) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardService.updateBoard(file, email, id, updateBoardDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시판 수정 성공")
                .build());
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<CommonResponse> deleteBoard(HttpServletRequest request, @PathVariable String id) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardService.deleteBoard(email, id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시판 삭제 성공")
                .build());
    }

    @PostMapping("/board/{id}/likes")
    public ResponseEntity<CommonResponse> updateLikes(HttpServletRequest request, @PathVariable String id) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardService.updateLikes(email, id);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시글 좋아요 성공")
                .build());
    }

    @DeleteMapping("/board/{id}/likes/{likesId}")
    public ResponseEntity<CommonResponse> deleteLikes(HttpServletRequest request, @PathVariable String id, @PathVariable String likesId) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardService.deleteLikes(email, likesId, id);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("게시글 좋아요 취소 성공")
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
            Page<ResponseBoard.BoardListDto> myBoardList = boardService.getMyBoard(email,pageable);

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
        Page<ResponseBoard.LikeBoardDto> response = boardService.getLikeBoard(email, pageable);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("좋아요 한 게시판 조회 성공")
                .list(response)
                .build());
    }
}
