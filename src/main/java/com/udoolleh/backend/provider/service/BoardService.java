package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.BoardServiceInterface;
import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.User;
import com.udoolleh.backend.repository.BoardRepository;
import com.udoolleh.backend.repository.UserRepository;
import com.udoolleh.backend.web.dto.RequestBoard;
import com.udoolleh.backend.web.dto.ResponseBoard;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService implements BoardServiceInterface {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    @Transactional
    @Override
    public void registerPosts(RequestBoard.Creates requestDto) {
        User user = userRepository.findByEmail(requestDto.getUser().getEmail());

        Board board = boardRepository.findByUser(user);

        board = Board.builder()
                .user(user)
                .title(requestDto.getTitle())
                .context(requestDto.getContext())
                .build();
        boardRepository.save(board);
        user.addBoard(board);
    }

    @Transactional
    @Override
    public void modifyPosts(RequestBoard.Updates requestDto) {

    }


}
