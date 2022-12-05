package com.udoolleh.backend.repository;

import com.udoolleh.backend.entity.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepository extends JpaRepository<Ads, String> {
}
