package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Likes;
import com.udoolleh.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes,String> {

    List<Likes> findByUser(User user);
}
