package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Board;

import com.udoolleh.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {
    Board findByTitleAndContext(String title, String context);

    Board findByUserAndId(User user, String id);

    @Query(value = "SELECT b FROM Board b WHERE b.id IN (SELECT l.board FROM Likes l inner join l.user Where l.user = :user)")
    Page<Board> findLikeBoard(User user, Pageable pageable);
 }
