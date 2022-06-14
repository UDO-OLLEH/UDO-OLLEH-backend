package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Wharf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WharfRepository extends JpaRepository<Wharf, String> {
    Wharf findByWharf(String wharfName);
}
