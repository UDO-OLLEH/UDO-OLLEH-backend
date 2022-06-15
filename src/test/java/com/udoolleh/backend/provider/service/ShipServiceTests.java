package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.type.ShipTimetableType;
import com.udoolleh.backend.entity.Wharf;
import com.udoolleh.backend.entity.WharfTimetable;
import com.udoolleh.backend.repository.WharfRepository;
import com.udoolleh.backend.repository.WharfTimetableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    @DisplayName("배 시간 등록 테스트")
    @Transactional
    @Test
    void registerWharfTimetableTest() throws ParseException {
        shipService.registerWharf("성산봉");
        Wharf wharf=wharfRepository.findByWharf("성산봉");

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date time = format.parse("08:10:00");
        Date otherTime = format.parse("08:11:00");
        shipService.registerWharfTimetable("성산봉", time, ShipTimetableType.typeOne);
        shipService.registerWharfTimetable("성산봉", otherTime,ShipTimetableType.typeOne);

        List<WharfTimetable> wharfTimetableList = wharfTimetableRepository.findByWharf(wharf);
        assertNotNull(wharfTimetableList);
        System.out.println(wharfTimetableList.get(0).getWharf().getWharf()+" "+wharfTimetableList.get(0).getDepartureTime()+" "+wharfTimetableList.get(0).getMonthType().getMonth());
        System.out.println(wharfTimetableList.get(1).getWharf().getWharf()+" "+wharfTimetableList.get(1).getDepartureTime()+" "+wharfTimetableList.get(1).getMonthType().getMonth());

    }

}
