package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Likes;
import com.udoolleh.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, String> {
    Optional<Likes> findByUserAndBoard(User user, Board board);

    int countByBoard(Board board);
}
