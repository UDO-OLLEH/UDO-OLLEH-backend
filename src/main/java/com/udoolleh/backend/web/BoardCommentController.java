package com.udoolleh.backend.web;

import com.udoolleh.backend.provider.security.JwtAuthToken;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.BoardCommentService;
import com.udoolleh.backend.web.dto.CommonResponse;
import com.udoolleh.backend.web.dto.RequestBoardComment;
import com.udoolleh.backend.web.dto.ResponseBoard;
import com.udoolleh.backend.web.dto.ResponseBoardComment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BoardCommentController {
    private final BoardCommentService boardCommentService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @PostMapping("/board/comment")
    public ResponseEntity<CommonResponse> registerBoardComment(HttpServletRequest request, @Valid @RequestBody RequestBoardComment.RegisterBoardCommentDto registerDto) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardCommentService.registerBoardComment(email, registerDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("댓글 등록 성공")
                .build());
    }
    @GetMapping("/board/{id}/comment")
    public ResponseEntity<CommonResponse> getBoardComment(HttpServletRequest request, @PathVariable String id) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        List<ResponseBoardComment.BoardCommentDto> boardCommentDtoList = boardCommentService.getBoardComment(email, id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("댓글 조회 성공")
                .list(boardCommentDtoList)
                .build());
    }
    @PutMapping("/board/comment")
    public ResponseEntity<CommonResponse> updateBoardComment(HttpServletRequest request, @Valid @RequestBody RequestBoardComment.UpdateBoardCommentDto updateDto) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardCommentService.modifyBoardComment(email, updateDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("댓글 수정 성공")
                .build());
    }
    @DeleteMapping("/board/comment/{id}")
    public ResponseEntity<CommonResponse> deleteBoardComment(HttpServletRequest request, @PathVariable String id) {
        Optional<String> token = jwtAuthTokenProvider.resolveToken(request);
        String email = null;
        if (token.isPresent()) {
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            email = jwtAuthToken.getData().getSubject();
        }
        boardCommentService.deleteBoardComment(email, id);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .message("댓글 삭제 성공")
                .build());
    }
}
