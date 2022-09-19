package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    User findByRefreshToken(String refreshToken);

    User findByNickname(String nickname);

}
