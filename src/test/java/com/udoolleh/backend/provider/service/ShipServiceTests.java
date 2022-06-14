package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Wharf;
import com.udoolleh.backend.repository.WharfRepository;
import com.udoolleh.backend.repository.WharfTimetableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class ShipServiceTests {
    @Autowired
    ShipService shipService;
    @Autowired
    WharfRepository wharfRepository;
    @Autowired
    WharfTimetableRepository wharfTimetableRepository;

    @DisplayName("선착장 등록 테스트")
    @Transactional
    @Test
    void registerStationTest(){
        shipService.registerWharf("성산봉");
        Wharf wharf = wharfRepository.findByWharf("성산봉");
        assertNotNull(wharf);
    }
}
