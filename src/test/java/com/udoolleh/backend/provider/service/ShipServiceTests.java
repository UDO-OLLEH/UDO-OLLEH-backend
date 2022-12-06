package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.entity.Harbor;
import com.udoolleh.backend.entity.HarborTimetable;
import com.udoolleh.backend.entity.ShipFare;
import com.udoolleh.backend.exception.errors.HarborNameDuplicatedException;
import com.udoolleh.backend.exception.errors.HarborPeriodDuplicatedException;
import com.udoolleh.backend.exception.errors.NotFoundHarborException;
import com.udoolleh.backend.exception.errors.NotFoundHarborTimetableException;
import com.udoolleh.backend.exception.errors.ShipFareDuplicatedException;
import com.udoolleh.backend.repository.HarborRepository;
import com.udoolleh.backend.repository.HarborTimetableRepository;
import com.udoolleh.backend.repository.ShipFareRepository;
import com.udoolleh.backend.web.dto.RequestShipFare;
import com.udoolleh.backend.web.dto.ResponseHarbor;
import com.udoolleh.backend.web.dto.ResponseHarborTimetable;
import com.udoolleh.backend.web.dto.ResponseShipFare;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
public class ShipServiceTests {
    @Autowired
    private ShipService shipService;
    @Autowired
    private HarborRepository harborRepository;
    @Autowired
    private HarborTimetableRepository harborTimetableRepository;
    @Autowired
    private ShipFareRepository shipFareRepository;

    private Long setUpHarborId;

    @BeforeEach
    void setUp() {
        Harbor harbor = Harbor.builder()
                .harborName("성산항")
                .build();
        harbor = harborRepository.save(harbor);
        setUpHarborId = harbor.getId();
    }

    @DisplayName("항구 등록 테스트")
    @Transactional
    @Test
    void registerHarborCourseTest() {
        shipService.registerHarbor("종달항");
        Harbor harbor = harborRepository.findByHarborName("종달항");
        assertNotNull(harbor);
    }

    @DisplayName("항구 중복 등록 예외 테스트")
    @Transactional
    @Test
    void registerSameHarborTest() {
        shipService.registerHarbor("종달항");
        assertThrows(HarborNameDuplicatedException.class, () -> shipService.registerHarbor("종달항"));
    }

    @DisplayName("배 시간 등록 테스트")
    @Transactional
    @Test
    void registerHarborTimetableTest() {
        //배 시간 엔티티에 잘 등록 되었는지 확인
        shipService.registerHarborTimetable("성산항", "하우목동항", "1,2월", "07:00 ~ 17:30");
        HarborTimetable harborTimetable = harborTimetableRepository.findByDestinationAndPeriodAndHarborId("하우목동항", "1,2월", setUpHarborId);
        assertNotNull(harborTimetable);

        //항구(부모)에 잘 등록되어 있는지 확인
        List<HarborTimetable> harborTimetables = harborRepository.findByHarborName("성산항").getHarborTimetables();
        HarborTimetable harborTimetableFromHarbor = harborTimetables.get(0);
        assertThat(harborTimetableFromHarbor).isEqualTo(harborTimetable);
    }

    @DisplayName("배 시간 중복 등록 테스트")
    @Transactional
    @Test
    void registerSameHarborTimetableTest() {
        shipService.registerHarborTimetable("성산항", "하우목동항", "1,2월", "07:00 ~ 17:30");

        assertThrows(HarborPeriodDuplicatedException.class, () -> shipService.registerHarborTimetable("성산항", "하우목동항", "1,2월", "07:00 ~ 17:30"));
    }

    @DisplayName("배 요금 등록 테스트")
    @Transactional
    @Test
    void registerShipFareTest() {
        RequestShipFare.RegisterShipFareDto shipFareDto =
                RequestShipFare.RegisterShipFareDto.builder()
                        .ageGroup("성인")
                        .leaveIsland(5000)
                        .enterIsland(2000)
                        .roundTrip(10000)
                        .harborId(setUpHarborId)
                        .build();

        shipService.registerShipFare(shipFareDto);
        assertThat(shipFareRepository.findAll()).isNotEmpty();
    }

    @DisplayName("배 요금 중복 등록 테스트")
    @Transactional
    @Test
    void registerSameShipFareTest() {
        RequestShipFare.RegisterShipFareDto shipFareDto =
                RequestShipFare.RegisterShipFareDto.builder()
                        .ageGroup("성인")
                        .leaveIsland(5000)
                        .enterIsland(2000)
                        .roundTrip(10000)
                        .harborId(setUpHarborId)
                        .build();
        RequestShipFare.RegisterShipFareDto sameAgeGroupShipFareDto =
                RequestShipFare.RegisterShipFareDto.builder()
                        .ageGroup("성인")
                        .leaveIsland(4000)
                        .enterIsland(2000)
                        .roundTrip(50000)
                        .harborId(setUpHarborId)
                        .build();
        shipService.registerShipFare(shipFareDto);

        assertThrows(ShipFareDuplicatedException.class, () -> shipService.registerShipFare(sameAgeGroupShipFareDto));
    }

    @DisplayName("배 요금 조회 테스트")
    @Transactional
    @Test
    void getShipFareTest() {
        //given
        Harbor setUpHarbor = harborRepository.findById(setUpHarborId).orElse(null);
        ShipFare shipFare = ShipFare.builder()
                .harbor(harborRepository.findById(setUpHarborId).orElse(null))
                .ageGroup("중학생")
                .leaveIsland(2000)
                .enterIsland(5000)
                .roundTrip(8000)
                .build();
        shipFareRepository.save(shipFare);
        setUpHarbor.addShipFare(shipFare);

        shipFare = ShipFare.builder()
                .harbor(harborRepository.findById(setUpHarborId).orElse(null))
                .ageGroup("성인")
                .leaveIsland(5500)
                .enterIsland(6000)
                .roundTrip(12000)
                .build();
        shipFareRepository.save(shipFare);
        setUpHarbor.addShipFare(shipFare);

        //when
        ResponseShipFare.HarborShipFare resultHarborShipFare = shipService.getShipFare(setUpHarborId);
        //then
        assertThat(resultHarborShipFare.getShipFares().size()).isEqualTo(2);
        assertThat(resultHarborShipFare.getHarborName()).isEqualTo("성산항");
    }

    @DisplayName("항구 조회 테스트")
    @Transactional
    @Test
    void getAllHarborsTest() {
        //given
        Harbor harbor = Harbor.builder()
                .harborName("종달항")
                .build();
        harborRepository.save(harbor);

        List<String> expectedResult = List.of("성산항", "종달항");
        //when
        List<ResponseHarbor.HarborDto> harborDtos = shipService.getAllHarbors();
        List<String> result = harborDtos.stream()
                .map(ResponseHarbor.HarborDto::getName)
                .collect(Collectors.toList());
        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    @DisplayName("배 시간 조회 테스트")
    @Transactional
    @ParameterizedTest
    @CsvSource({
            "1 ~ 2월, 07:30 ~ 17:30, 하우목동항",
            "4 ~ 6월, 07:00 ~ 17:00, 천진항"
    })
    void getHarborTimetableTest(String period, String operatingTime, String destination) {
        //given
        Harbor harbor = harborRepository.findByHarborName("성산항");
        HarborTimetable harborTimetable = HarborTimetable.builder()
                .harbor(harbor)
                .destination(destination)
                .period(period)
                .operatingTime(operatingTime)
                .build();
        harbor.addHarborTimetable(harborTimetable);
        harborTimetable = harborTimetableRepository.save(harborTimetable);
        ResponseHarborTimetable.TimetableDto timetableDto = ResponseHarborTimetable.TimetableDto.builder()
                .id(harborTimetable.getId())
                .period(period)
                .operatingTime(operatingTime)
                .build();
        ResponseHarborTimetable.HarborTimetableDto harborTimetableDto = ResponseHarborTimetable.HarborTimetableDto.builder()
                .timetableDto(List.of(timetableDto))
                .destination(destination)
                .build();

        //when
        ResponseHarborTimetable.HarborTimetableDto result = shipService.getHarborTimetable("성산항", destination);

        //then
        assertThat(result).isEqualTo(harborTimetableDto);
    }

    @DisplayName("배 시간이 존재하지 않거나 항구가 등록되어 있지 않을 경우 테스트")
    @Transactional
    @Test
    void getEmptyHarborOrEmptyTimetableTest() {
        assertThrows(NotFoundHarborException.class, () -> shipService.getHarborTimetable("등록되지 않은 항구", "존재하지않은 목적지"));   //항구가 등록되어 있지 않을 경우
        assertThrows(NotFoundHarborTimetableException.class, () -> shipService.getHarborTimetable("성산항", "존재하지 않는 목적지"));    //시간이 존재하지 않는 경우
    }

    @DisplayName("항구 삭제 테스트")
    @Transactional
    @Test
    void deleteHarborTest() {
        Long harborId = harborRepository.findByHarborName("성산항").getId();
        shipService.deleteHarbor(harborId);

        List<Harbor> result = harborRepository.findAll();

        assertThat(result).isEmpty();
    }

    @DisplayName("항구 삭제시 배 시간표 및 배 요금 삭제 테스트")
    @Transactional
    @Test
    void deleteHarborWithHarborTimetableTest() {
        Harbor setUpHarbor = harborRepository.findByHarborName("성산항");
        HarborTimetable harborTimetable = HarborTimetable.builder()
                .operatingTime("07:30 ~ 17:30")
                .period("1 ~ 2월")
                .destination("하우목동항")
                .harbor(setUpHarbor)
                .build();

        ShipFare shipFare = ShipFare.builder()
                .harbor(setUpHarbor)
                .ageGroup("중학생")
                .leaveIsland(2000)
                .enterIsland(5000)
                .roundTrip(8000)
                .build();

        harborTimetableRepository.save(harborTimetable);
        setUpHarbor.addHarborTimetable(harborTimetable);
        setUpHarbor.addShipFare(shipFare);

        shipService.deleteHarbor(setUpHarborId);

        assertThat(harborTimetableRepository.findAll()).isEmpty();
        assertThat(shipFareRepository.findAll()).isEmpty();
    }

    @DisplayName("배 시간 삭제 테스트")
    @Transactional
    @Test
    void deleteHarborTimetableTest() {
        Harbor setUpHarbor = harborRepository.findByHarborName("성산항");
        HarborTimetable harborTimetable = HarborTimetable.builder()
                .operatingTime("07:30 ~ 17:30")
                .period("1 ~ 2월")
                .destination("하우목동항")
                .harbor(setUpHarbor)
                .build();
        HarborTimetable harborTimetableEntity = harborTimetableRepository.save(harborTimetable);
        setUpHarbor.addHarborTimetable(harborTimetableEntity);

        shipService.deleteHarborTimetable(harborTimetableEntity.getId());
        assertThat(harborTimetableRepository.findAll()).isEmpty();
        assertThat(setUpHarbor.getHarborTimetables()).isEmpty();

    }

    @DisplayName("배 요금 삭제 테스트")
    @Transactional
    @Test
    void deleteShipFareTest() {
        Harbor setUpHarbor = harborRepository.findById(setUpHarborId).orElse(null);
        ShipFare shipFare = ShipFare.builder()
                .harbor(setUpHarbor)
                .ageGroup("중학생")
                .leaveIsland(2000)
                .enterIsland(5000)
                .roundTrip(8000)
                .build();
        shipFareRepository.save(shipFare);
        setUpHarbor.addShipFare(shipFare);

        shipService.deleteShipFare(shipFare.getId());
        assertThat(shipFareRepository.findAll()).isEmpty();
        assertThat(setUpHarbor.getShipFares()).isEmpty();
    }
}
