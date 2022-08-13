package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Board;

import com.udoolleh.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, String> {
    Board findByTitleAndContext(String title, String context);

    Board findByUserAndBoardId(User user, String boardId);
    Page<Board> findAllByOrderByBoardIdDesc(Pageable pageable);

    Board findByBoardId(String boardId);
    //Repository 내에서 사용되지 않는 쿼리 메소드는 지운다.
}
