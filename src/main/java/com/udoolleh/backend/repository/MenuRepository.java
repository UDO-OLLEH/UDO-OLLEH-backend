package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
