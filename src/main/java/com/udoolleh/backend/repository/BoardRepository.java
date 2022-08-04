package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.User;
import org.hibernate.type.StringNVarcharType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByUser(User user);
}
