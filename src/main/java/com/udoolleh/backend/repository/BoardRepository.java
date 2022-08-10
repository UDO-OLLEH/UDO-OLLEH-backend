package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Board;

import com.udoolleh.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BoardRepository extends JpaRepository<Board, String> {
    Board findByTitleAndContext(String title, String context);
    Board findByUserAndBoardId(User user, String boardId);

}
