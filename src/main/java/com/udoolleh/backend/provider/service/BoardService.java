package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.BoardServiceInterface;
import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.CustomJwtRuntimeException;
import com.udoolleh.backend.exception.errors.NotFoundBoardException;
import com.udoolleh.backend.exception.errors.NotFoundUserException;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.ResponseBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService implements BoardServiceInterface {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //게시글 전체 조회
    @Transactional(readOnly = true)
    @Override
    public Page<ResponseBoard.ListBoard> boardList(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }

        Page<Board> board = boardRepository.findAllByOrderByBoardIdDesc(pageable);
        return board.map(ResponseBoard.ListBoard::of);

    }


    //게시글 등록 API
    @Override
    @Transactional
    public void registerPosts(String userEmail, RequestBoard.Register postDto) { //postDto = title, context + category 추후 추가
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new CustomJwtRuntimeException();
        }

        //Board board = boardRepository.findByTitleContext(postDto.getTitle(), postDto.getContext);
        //if (board == null) { ~ }
        //빌더 패턴으로 게시글 작성
        Board board = Board.builder()
                .title(postDto.getTitle())
                .context(postDto.getContext())
                .user(user)
                .build();
        //게시글을 등록하고 저장
        board = boardRepository.save(board);
        user.addBoard(board);
    }

    @Override
    @Transactional
    public void modifyPosts(String userEmail, String boardId, RequestBoard.Updates updatesDto) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findByUserAndBoardId(user, boardId);
        if (board == null) {
            throw new NotFoundBoardException();
        }
        board.modifyPosts(updatesDto.getTitle(), updatesDto.getContext());
    }

    @Override
    @Transactional
    public void deletePosts(String userEmail, String boardId) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new CustomJwtRuntimeException();
        }
        Board board = boardRepository.findByUserAndBoardId(user, boardId);
        if (board == null) {
            throw new NotFoundBoardException();
        }

        user.getBoardList().remove(board);
        boardRepository.deleteById(boardId);
    }

}
