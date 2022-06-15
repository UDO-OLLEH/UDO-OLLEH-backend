package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.ShipServiceInterface;
import com.udoolleh.backend.core.type.ShipTimetableType;
import com.udoolleh.backend.entity.Wharf;
import com.udoolleh.backend.entity.WharfTimetable;
import com.udoolleh.backend.exception.errors.NotFoundWharfException;
import com.udoolleh.backend.exception.errors.WharfNameDuplicatedException;
import com.udoolleh.backend.exception.errors.WharfTimeDuplicatedException;
import com.udoolleh.backend.repository.WharfRepository;
import com.udoolleh.backend.repository.WharfTimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ShipService implements ShipServiceInterface {
    private final WharfRepository wharfRepository;
    private final WharfTimetableRepository wharfTimetableRepository;

    @Transactional
    @Override
    public void registerWharf(String wharfName){
        Wharf wharf = wharfRepository.findByWharf(wharfName);
        if(wharf != null){ //이미 존재하면
            throw new WharfNameDuplicatedException();
        }
        wharf = Wharf.builder()
                .wharf(wharfName)
                .build();
        //선착장 등록
        wharfRepository.save(wharf);
    }
    @Transactional
    @Override
    public void registerWharfTimetable(String wharfName, Date departureTime, ShipTimetableType monthType){
        Wharf wharf = wharfRepository.findByWharf(wharfName);
        if(wharf == null){ //선착장이 없으면 예외 던지기
            throw new NotFoundWharfException();
        }
        WharfTimetable wharfTime = wharfTimetableRepository.findByWharfAndDepartureTimeAndMonthType(wharf, departureTime, monthType);
        if(wharfTime != null){// 이미 있는 시간이면 예외 던지기
            throw new WharfTimeDuplicatedException();
        }
        //시간 추가
        WharfTimetable wharfTimetable = WharfTimetable.builder()
                .departureTime(departureTime)
                .wharf(wharf)
                .monthType(monthType)
                .build();
        wharfTimetableRepository.save(wharfTimetable);
        wharf.addTimetable(wharfTimetable);
    }

}
