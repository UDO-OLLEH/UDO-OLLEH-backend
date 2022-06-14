package com.udoolleh.backend.provider;

import com.udoolleh.backend.core.service.ShipServiceInterface;
import com.udoolleh.backend.entity.Wharf;
import com.udoolleh.backend.exception.errors.WharfNameDuplicatedException;
import com.udoolleh.backend.repository.WharfRepository;
import com.udoolleh.backend.repository.WharfTimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
