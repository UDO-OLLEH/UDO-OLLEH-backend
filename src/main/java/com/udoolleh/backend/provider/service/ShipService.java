package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.ShipServiceInterface;
import com.udoolleh.backend.entity.Harbor;
import com.udoolleh.backend.entity.HarborTimetable;
import com.udoolleh.backend.exception.errors.HarborNameDuplicatedException;
import com.udoolleh.backend.exception.errors.HarborPeriodDuplicatedException;
import com.udoolleh.backend.exception.errors.NotFoundHarborException;
import com.udoolleh.backend.exception.errors.NotFoundHarborTimetableException;
import com.udoolleh.backend.repository.HarborRepository;
import com.udoolleh.backend.repository.HarborTimetableRepository;
import com.udoolleh.backend.web.dto.ResponseHarbor;
import com.udoolleh.backend.web.dto.ResponseHarborTimetable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShipService implements ShipServiceInterface {
    private final HarborRepository harborRepository;
    private final HarborTimetableRepository harborTimetableRepository;

    @Transactional
    @Override
    public void registerHarbor(String harborName) {
        Harbor harbor = harborRepository.findByHarborName(harborName);
        if (harbor != null) { //이미 존재하면
            throw new HarborNameDuplicatedException();
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
            throw new NotFoundHarborException();
        }
        HarborTimetable harborTimetable = harborTimetableRepository.findByDestinationAndPeriodAndHarborId(destination, period, harbor.getId());
        if (harborTimetable != null) {// 이미 있는 기간이면 예외 던지기
            throw new HarborPeriodDuplicatedException();
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

    @Transactional(readOnly = true)
    @Override
    public List<ResponseHarbor.HarborDto> getAllHarbors() {
        List<Harbor> harbors = harborRepository.findAll();
        if (harbors.isEmpty()) {
            return Collections.emptyList();
        }
        List<ResponseHarbor.HarborDto> harborDtos = new ArrayList<>();

        for(Harbor harbor : harbors) {
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
    public ResponseHarborTimetable.HarborTimetableDto getHarborTimetable(String harborName, String destination) {
        Harbor harbor = harborRepository.findByHarborName(harborName);
        if (harbor == null) {
            throw new NotFoundHarborException();
        }

        List<HarborTimetable> harborTimetables = harborTimetableRepository.findByDestinationAndHarbor(destination, harbor);
        if (harborTimetables.isEmpty()) {
            throw new NotFoundHarborTimetableException();
        }
        List<ResponseHarborTimetable.TimetableDto> timetableDtos = new ArrayList<>();

        for (HarborTimetable harborTimetable : harborTimetables) {
            timetableDtos.add(ResponseHarborTimetable.TimetableDto.builder()
                    .id(harborTimetable.getId())
                    .operatingTime(harborTimetable.getOperatingTime())
                    .period(harborTimetable.getPeriod())
                    .build());
        }

        ResponseHarborTimetable.HarborTimetableDto harborTimetableDto = ResponseHarborTimetable.HarborTimetableDto.builder()
                .timetableDto(timetableDtos)
                .destination(harborTimetables.get(0).getDestination())
                .build();
        return harborTimetableDto;
    }

    @Transactional
    @Override
    public void deleteHarbor(Long harborId) {
        Harbor harbor = harborRepository.findById(harborId).orElseThrow(() -> new NotFoundHarborException());
        harborRepository.delete(harbor);
    }

    @Transactional
    @Override
    public void deleteHarborTimetable(Long harborTimetableId) {
        harborTimetableRepository.findById(harborTimetableId).orElseThrow(() -> new NotFoundHarborTimetableException());
        harborTimetableRepository.deleteById(harborTimetableId);
    }
}
