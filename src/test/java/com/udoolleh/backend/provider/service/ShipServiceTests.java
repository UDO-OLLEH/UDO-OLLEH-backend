package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.type.ShipCourseType;
import com.udoolleh.backend.core.type.ShipTimetableType;
import com.udoolleh.backend.entity.Wharf;
import com.udoolleh.backend.entity.WharfTimetable;
import com.udoolleh.backend.exception.errors.NotFoundWharfException;
import com.udoolleh.backend.repository.WharfRepository;
import com.udoolleh.backend.repository.WharfTimetableRepository;
import com.udoolleh.backend.web.dto.ResponseWharfTimetable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void registerWharfCourseTest(){
        shipService.registerWharfCourse(ShipCourseType.JONGDAL_UDO);
        Wharf wharf = wharfRepository.findByWharfCourse(ShipCourseType.JONGDAL_UDO);
        assertNotNull(wharf);
        System.out.println(wharf.getWharfCourse());
    }
    @DisplayName("배 시간 등록 테스트")
    @Transactional
    @Test
    void registerWharfTimetableTest() throws ParseException {
        shipService.registerWharfCourse(ShipCourseType.SEONGSAN_UDO);
        Wharf wharf = wharfRepository.findByWharfCourse(ShipCourseType.SEONGSAN_UDO);
        List<String> time =new ArrayList<>();
        List<String> otherTime =new ArrayList<>();
        time.add("8:00");
        time.add("8:30");
        time.add("9:00");
        shipService.registerWharfTimetable(ShipCourseType.SEONGSAN_UDO, time, ShipTimetableType.SEONGSAN_One);

        List<WharfTimetable> wharfTimetableList = wharfTimetableRepository.findByWharf(wharf);
        assertNotNull(wharfTimetableList);
        System.out.println(wharfTimetableList.get(0).getWharf().getWharfCourse()+" "+wharfTimetableList.get(0).getDepartureTime()+" "+wharfTimetableList.get(0).getMonthType().getMonth());
        System.out.println(wharfTimetableList.get(1).getWharf().getWharfCourse()+" "+wharfTimetableList.get(1).getDepartureTime()+" "+wharfTimetableList.get(1).getMonthType().getMonth());
    }
    @DisplayName("선착장 조회 테스트")
    @Transactional
    @Test
    void getAllWharfTest(){
        Wharf wharf = Wharf.builder()
                .wharfCourse(ShipCourseType.SEONGSAN_UDO)
                .build();
        wharfRepository.save(wharf);
        Wharf otherWharf = Wharf.builder()
                .wharfCourse(ShipCourseType.UDO_JONGDAL)
                .build();
        wharfRepository.save(otherWharf);
        List<String> wharfList = shipService.getAllWharf().orElseThrow(()-> new NotFoundWharfException());
        assertNotNull(wharfList);
        System.out.println(wharfList.get(0)+" "+wharfList.get(1));
    }
    @DisplayName("배 시간 조회 테스트")
    @Transactional
    @Test
    void getWharfTimetableTest(){
        Wharf wharf = Wharf.builder()
                .wharfCourse(ShipCourseType.SEONGSAN_UDO)
                .build();
        wharfRepository.save(wharf);
        List<String> time = new ArrayList<>();
        time.add("8:00");
        time.add("8:30");
        time.add("18:00");
        time.add("18:30");
        shipService.registerWharfTimetable(ShipCourseType.SEONGSAN_UDO, time, ShipTimetableType.SEONGSAN_One);
        ResponseWharfTimetable.WharfTimetable wharfTimetable = shipService.getWharfTimetable(ShipCourseType.SEONGSAN_UDO, ShipTimetableType.SEONGSAN_One).orElseThrow(() -> null);
        assertNotNull(wharfTimetable);
        System.out.println(wharfTimetable.getDepartureTime()+" "+wharfTimetable.getWharfCourse()+" "+wharfTimetable.getMonthType());
    }
    @DisplayName("선착장 삭제 테스트")
    @Transactional
    @Test
    void deleteWharfTest(){
        Wharf wharf = Wharf.builder()
                .wharfCourse(ShipCourseType.SEONGSAN_UDO)
                .build();
        wharfRepository.save(wharf);
        assertNotNull(wharfRepository.findAll());
        shipService.deleteWharf(ShipCourseType.SEONGSAN_UDO);
        List<String> emptyArray = new ArrayList<>();
        assertEquals(emptyArray ,wharfRepository.findAll());
    }
    @DisplayName("배 시간 삭제 테스트")
    @Transactional
    @Test
    void deleteWharfTimetableTest(){
        Wharf wharf = Wharf.builder()
                .wharfCourse(ShipCourseType.SEONGSAN_UDO)
                .build();
        wharfRepository.save(wharf);
        List<String> time = new ArrayList<>();
        time.add("8:00");
        time.add("8:30");
        time.add("18:00");
        time.add("18:30");
        for(int i=0; time.size()>i; i++) {
            WharfTimetable wharfTimetable = WharfTimetable.builder()
                    .departureTime(time.get(i))
                    .wharf(wharf)
                    .monthType(ShipTimetableType.SEONGSAN_One)
                    .build();
            wharfTimetableRepository.save(wharfTimetable);
            wharf.addTimetable(wharfTimetable);
        }
        assertNotNull(wharfTimetableRepository.findAll());
        shipService.deleteWharfTimetable(ShipCourseType.SEONGSAN_UDO, ShipTimetableType.SEONGSAN_One);
        List<String> emptyArray = new ArrayList<>();
        assertEquals(emptyArray ,wharfTimetableRepository.findAll());
    }

}
