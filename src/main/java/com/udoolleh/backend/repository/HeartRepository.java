package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Heart;
import com.udoolleh.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, String> {
    Optional<Heart> findByUserAndBoard(User user, Board board);

    int countByBoard(Board board);
}
