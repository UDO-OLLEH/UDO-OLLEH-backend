package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.ShipServiceInterface;
import com.udoolleh.backend.core.type.ShipCourseType;
import com.udoolleh.backend.core.type.ShipTimetableType;
import com.udoolleh.backend.entity.Wharf;
import com.udoolleh.backend.entity.WharfTimetable;
import com.udoolleh.backend.exception.errors.NotFoundWharfException;
import com.udoolleh.backend.exception.errors.NotFoundWharfTimetableException;
import com.udoolleh.backend.exception.errors.WharfNameDuplicatedException;
import com.udoolleh.backend.exception.errors.WharfTimeDuplicatedException;
import com.udoolleh.backend.repository.WharfRepository;
import com.udoolleh.backend.repository.WharfTimetableRepository;
import com.udoolleh.backend.web.dto.ResponseWharfTimetable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipService implements ShipServiceInterface {
    private final WharfRepository wharfRepository;
    private final WharfTimetableRepository wharfTimetableRepository;

    @Transactional
    @Override
    public void registerWharfCourse(ShipCourseType wharfCourse){
        Wharf wharf = wharfRepository.findByWharfCourse(wharfCourse);
        if(wharf != null){ //이미 존재하면
            throw new WharfNameDuplicatedException();
        }
        wharf = Wharf.builder()
                .wharfCourse(wharfCourse)
                .build();
        //선착장 등록
        wharfRepository.save(wharf);
    }
    @Transactional
    @Override
    public void registerWharfTimetable(ShipCourseType wharfCourse, List<String> departureTime, ShipTimetableType monthType){
        Wharf wharf = wharfRepository.findByWharfCourse(wharfCourse);
        if(wharf == null){ //선착장이 없으면 예외 던지기
            throw new NotFoundWharfException();
        }
        List<WharfTimetable> wharfTimeList = wharfTimetableRepository.findByWharfAndMonthType(wharf, monthType);
        if(!wharfTimeList.isEmpty()){// 이미 있는 시간이면 예외 던지기
            throw new WharfTimeDuplicatedException();
        }
        //시간 추가
        for(int i=0; departureTime.size()>i; i++) {
            WharfTimetable wharfTimetable = WharfTimetable.builder()
                    .departureTime(departureTime.get(i))
                    .wharf(wharf)
                    .monthType(monthType)
                    .build();
            wharfTimetableRepository.save(wharfTimetable);
            wharf.addTimetable(wharfTimetable);
        }
    }
    @Transactional(readOnly = true)
    @Override
    public Optional<List<String>> getAllWharf(){
        List<Wharf> findAllWharf = wharfRepository.findAll();
        List<String> wharfList = new ArrayList<>();

        if(!findAllWharf.isEmpty()) {
            for (Wharf wharf : findAllWharf) {
                wharfList.add(wharf.getWharfCourse().getDestination());
            }
        }
        return Optional.ofNullable(wharfList);
    }
    @Transactional(readOnly = true)
    @Override
    public Optional<ResponseWharfTimetable.WharfTimetableDto> getWharfTimetable(ShipCourseType wharfCourse, ShipTimetableType monthType){
        Wharf wharf = wharfRepository.findByWharfCourse(wharfCourse);
        if(wharf == null){ //선착장이 없으면 예외 던지기
            throw new NotFoundWharfException();
        }
        List<WharfTimetable> wharfTimetableList = wharfTimetableRepository.findByWharfAndMonthType(wharf,monthType);
        List<String> time = new ArrayList<>();
        if(!wharfTimetableList.isEmpty()){
            for(WharfTimetable wharfTimetable : wharfTimetableList){
                time.add(wharfTimetable.getDepartureTime());
            }
        }
        ResponseWharfTimetable.WharfTimetableDto resWharfTimetable = ResponseWharfTimetable.WharfTimetableDto.builder()
                .monthType(monthType.getMonth())
                .departureTime(time)
                .wharfCourse(wharf.getWharfCourse().getDestination())
                .build();

        return Optional.ofNullable(resWharfTimetable);
    }

    @Transactional
    @Override
    public void deleteWharf(ShipCourseType wharfCourse){
        Wharf res = wharfRepository.findByWharfCourse(wharfCourse);
        if(wharfCourse == null){ //선착장이 없으면 예외 던지기
            throw new NotFoundWharfException();
        }
        wharfRepository.delete(res);
    }
    @Transactional
    @Override
    public void deleteWharfTimetable(ShipCourseType wharfCourse, ShipTimetableType monthType){
        Wharf wharf = wharfRepository.findByWharfCourse(wharfCourse);
        if(wharfCourse == null){ //선착장이 없으면 예외 던지기
            throw new NotFoundWharfException();
        }
        List<WharfTimetable> res = wharfTimetableRepository.findByWharfAndMonthType(wharf, monthType);
        if(res.size() == 0){
            throw new NotFoundWharfTimetableException();
        }

        wharfTimetableRepository.deleteWharfTimetableList(res);
    }
}
