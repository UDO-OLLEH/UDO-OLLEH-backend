package com.udoolleh.backend.ads;

import com.udoolleh.backend.ads.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepository extends JpaRepository<Ads, String> {
}
