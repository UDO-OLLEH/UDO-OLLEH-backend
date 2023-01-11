package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.ShipServiceInterface;
import com.udoolleh.backend.entity.Harbor;
import com.udoolleh.backend.entity.HarborTimetable;
import com.udoolleh.backend.entity.ShipFare;
import com.udoolleh.backend.exception.CustomException;
import com.udoolleh.backend.exception.ErrorCode;
import com.udoolleh.backend.repository.HarborRepository;
import com.udoolleh.backend.repository.HarborTimetableRepository;
import com.udoolleh.backend.repository.ShipFareRepository;
import com.udoolleh.backend.web.dto.RequestShipFare;
import com.udoolleh.backend.web.dto.ResponseHarbor;
import com.udoolleh.backend.web.dto.ResponseHarborTimetable;
import com.udoolleh.backend.web.dto.ResponseShipFare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShipService implements ShipServiceInterface {
    private final HarborRepository harborRepository;
    private final HarborTimetableRepository harborTimetableRepository;
    private final ShipFareRepository shipFareRepository;

    @Transactional
    @Override
    public void registerHarbor(String harborName) {
        Harbor harbor = harborRepository.findByHarborName(harborName);
        if (harbor != null) { //이미 존재하면
            throw new CustomException(ErrorCode.HARBOR_NAME_DUPLICATED);
        }
        harbor = harbor.builder()
                .harborName(harborName)
                .build();
        //선착장 등록
        harborRepository.save(harbor);
    }

    //harbor가 다르면 항과 시간이 같아도 등록 가능하도록 해야함
    @Transactional
    @Override
    public void registerHarborTimetable(String harborName, String destination, String period, String operatingTime) {
        Harbor harbor = harborRepository.findByHarborName(harborName);
        if (harbor == null) { //선착장이 없으면 예외 던지기
            throw new CustomException(ErrorCode.NOT_FOUND_HARBOR);
        }
        HarborTimetable harborTimetable = harborTimetableRepository.findByDestinationAndPeriodAndHarborId(destination, period, harbor.getId());
        if (harborTimetable != null) {// 이미 있는 기간이면 예외 던지기
            throw new CustomException(ErrorCode.HARBOR_PERIOD_DUPLICATED);
        }
        //시간 추가
        harborTimetable = HarborTimetable.builder()
                .destination(destination)
                .harbor(harbor)
                .operatingTime(operatingTime)
                .period(period)
                .build();
        harborTimetableRepository.save(harborTimetable);
        harbor.addHarborTimetable(harborTimetable);
    }

    @Transactional
    @Override
    public void registerShipFare(RequestShipFare.RegisterShipFareDto registerShipFareDto) {
        Harbor harbor = harborRepository.findById(registerShipFareDto.getHarborId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HARBOR));
        if (shipFareRepository.findByAgeGroupAndHarborId(registerShipFareDto.getAgeGroup(), registerShipFareDto.getHarborId()) != null) {
            throw new CustomException(ErrorCode.SHIP_FARE_DUPLICATED);
        }

        ShipFare shipFare = ShipFare.builder()
                .ageGroup(registerShipFareDto.getAgeGroup())
                .roundTrip(registerShipFareDto.getRoundTrip())
                .enterIsland(registerShipFareDto.getEnterIsland())
                .leaveIsland(registerShipFareDto.getLeaveIsland())
                .harbor(harbor)
                .build();
        shipFareRepository.save(shipFare);
        harbor.addShipFare(shipFare);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ResponseHarbor.HarborDto> getAllHarbors() {
        List<Harbor> harbors = harborRepository.findAll();
        if (harbors.isEmpty()) {
            return Collections.emptyList();
        }
        List<ResponseHarbor.HarborDto> harborDtos = new ArrayList<>();

        for (Harbor harbor : harbors) {
            ResponseHarbor.HarborDto harborDto = ResponseHarbor.HarborDto.builder()
                    .id(harbor.getId())
                    .name(harbor.getHarborName())
                    .build();
            harborDtos.add(harborDto);
        }
        return harborDtos;
    }

    //destination 이 다르면 조회되지 않도록
    //id를 함께 조회할 수 있도록 해야함
    @Transactional(readOnly = true)
    @Override
    public ResponseHarborTimetable.HarborTimetableDto getHarborTimetable(Long id) {
        Harbor harbor = harborRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HARBOR));
        List<HarborTimetable> harborTimetables = harborTimetableRepository.findByHarbor(harbor);
        if (harborTimetables.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_HARBOR_TIMETABLE);
        }

        List<ResponseHarborTimetable.TimetableDto> timetableDtos = new ArrayList<>();
        String destination = "";
        for (HarborTimetable harborTimetable : harborTimetables) {
            timetableDtos.add(ResponseHarborTimetable.TimetableDto.builder()
                    .id(harborTimetable.getId())
                    .operatingTime(harborTimetable.getOperatingTime())
                    .period(harborTimetable.getPeriod())
                    .build());
            destination = harborTimetable.getDestination();
        }

        ResponseHarborTimetable.HarborTimetableDto harborTimetableDto = ResponseHarborTimetable.HarborTimetableDto.builder()
                .timetableDtos(timetableDtos)
                .destination(destination)
                .build();
        return harborTimetableDto;
    }

    @Transactional
    @Override
    public ResponseShipFare.HarborShipFareDto getShipFare(Long harborId) {
        Harbor harbor = harborRepository.findById(harborId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HARBOR));

        List<ShipFare> shipFares = shipFareRepository.findByHarborId(harborId);

        if (shipFares.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_SHIP_FARE);
        }

        List<ResponseShipFare.ShipFareDto> responseShipFare = new ArrayList<>();
        for (ShipFare shipFare : shipFares) {
            responseShipFare.add(ResponseShipFare.ShipFareDto.builder()
                    .id(shipFare.getId())
                    .ageGroup(shipFare.getAgeGroup())
                    .roundTrip(shipFare.getRoundTrip())
                    .enterIsland(shipFare.getEnterIsland())
                    .leaveIsland(shipFare.getLeaveIsland())
                    .build());
        }

        ResponseShipFare.HarborShipFareDto responseHarborShipFare = ResponseShipFare.HarborShipFareDto.builder()
                .harborName(harbor.getHarborName())
                .shipFareDtos(responseShipFare)
                .build();

        return responseHarborShipFare;
    }

    @Transactional
    @Override
    public void deleteHarbor(Long harborId) {
        harborRepository.findById(harborId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HARBOR));

        harborRepository.deleteById(harborId);
    }

    @Transactional
    @Override
    public void deleteHarborTimetable(Long harborTimetableId) {
        HarborTimetable harborTimetable = harborTimetableRepository.findById(harborTimetableId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HARBOR_TIMETABLE));

        harborTimetable.getHarbor().getHarborTimetables().remove(harborTimetable);
        harborTimetableRepository.deleteById(harborTimetableId);
    }

    @Transactional
    @Override
    public void deleteShipFare(Long shipFareId) {
        ShipFare shipFare = shipFareRepository.findById(shipFareId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SHIP_FARE));

        shipFare.getHarbor().getShipFares().remove(shipFare);
        shipFareRepository.deleteById(shipFareId);
    }
}
