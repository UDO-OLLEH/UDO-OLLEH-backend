package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Harbor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarborRepository extends JpaRepository<Harbor, Long> {
    Harbor findByHarborName(String harborName);
}
