package com.mycompany.jwtdemo.util;

import com.mycompany.jwtdemo.entity.GstFiledEntity;
import com.mycompany.jwtdemo.model.GstTrackerDTO;
import com.mycompany.jwtdemo.model.GstTrackerDetail;
import com.mycompany.jwtdemo.model.GstTrackerWrapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GstUtil {

    public GstTrackerWrapper createGstWrapper(String gstNo){
        GstTrackerWrapper wrapper = new GstTrackerWrapper();
        wrapper.setGstNo(gstNo);
        return wrapper;
    }

    public List<GstTrackerDTO> createGstTrackerDTO(){
        List<GstTrackerDTO> trackerDTOs = new ArrayList<>();
        return trackerDTOs;
    }

    public GstTrackerDTO createGstTrackerDto(String returnType){
        GstTrackerDTO trackerDTO = new GstTrackerDTO();
        trackerDTO.setReturnType(returnType);
        return trackerDTO;
    }

    public List<GstTrackerDetail> createFiledAndNotFiledDetails(List<GstFiledEntity> entityList){
       List<GstTrackerDetail> detailList = calculateMonthandYearsForFiling(entityList);
        entityList.stream().forEach( e -> {
            detailList.stream().forEach(dl -> {
                if(e.getDateOfFiling().getYear() == dl.getYear() &&
                    e.getDateOfFiling().getMonth().toString() == dl.getMonth()) {
                    dl.setIsFiled(Boolean.TRUE);
                    dl.setDateOfFiling(e.getDateOfFiling());
                }
            });
        });
        return detailList;
    }

    public List<GstTrackerDetail> calculateMonthandYearsForFiling(List<GstFiledEntity> entityList){
        List<GstTrackerDetail> detailList = new ArrayList<>();
        GstTrackerDetail details;
        List<LocalDate> dates = entityList.stream()
                .map(e -> e.getDateOfFiling()).collect(Collectors.toList());
        LocalDate minDate = Collections.min(dates);
        LocalDate maxDate = Collections.max(dates);
        while(minDate.isBefore(maxDate)){
            details = new GstTrackerDetail();
            details.setMonth(minDate.getMonth().name());
            details.setYear(minDate.getYear());
            details.setIsFiled(Boolean.FALSE);
            minDate = minDate.plusMonths(1);
            detailList.add(details);
        }
        return detailList;
    }
}
