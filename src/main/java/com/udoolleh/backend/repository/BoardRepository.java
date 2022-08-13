package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Board;

import com.udoolleh.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, String> {
    Board findByTitleAndContext(String title, String context);

    Board findByUserAndBoardId(User user, String boardId);
    List<Board> findAllByOrderByBoardIdDesc();

    Page<Board> findByBoard(User user, Pageable pageable);

}
