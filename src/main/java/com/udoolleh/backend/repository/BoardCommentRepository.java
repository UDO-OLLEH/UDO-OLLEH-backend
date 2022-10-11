package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.BoardComment;
import com.udoolleh.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, String> {
    BoardComment findByContext(String context);
    List<BoardComment> findByBoard(Board board);
}
