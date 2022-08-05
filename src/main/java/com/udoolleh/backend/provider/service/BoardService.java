package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.BoardServiceInterface;
import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.exception.errors.NotFoundUserException;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService implements BoardServiceInterface {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    //게시글 등록 API
    @Override
    @Transactional
    public void registerPosts(String email, RequestBoard.Register postDto) { //requestDto = title, context + category 추후 추가
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new NotFoundUserException();
        }
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
    public void modifyPosts(RequestBoard.Updates requestDto) {

    }


}
