package com.mycompany.jwtdemo.service;

import com.mycompany.jwtdemo.entity.GstAccountEntity;
import com.mycompany.jwtdemo.entity.GstFiledEntity;
import com.mycompany.jwtdemo.model.GstTrackerDTO;
import com.mycompany.jwtdemo.model.GstTrackerWrapper;
import com.mycompany.jwtdemo.repository.GstAccountRepository;
import com.mycompany.jwtdemo.repository.GstFiledRepository;
import com.mycompany.jwtdemo.util.GstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class GstFiledService {

    @Autowired
    private GstFiledRepository filedRepository;
    @Autowired
    private GstApiCallService gstApiCallService;
    @Autowired
    private GstAccountRepository gstAccountRepository;
    @Autowired
    private GstUtil util;

    public GstTrackerWrapper getAllDetails(String gstNo, LocalDate customFromDate, LocalDate customToDate , String gstReturnType) {
        List<GstFiledEntity> filedEntities = filedRepository.findAllByGstNoAndDateOfFilingBetween(gstNo, customFromDate, customToDate);
        GstTrackerWrapper wrapper = util.createGstWrapper(gstNo);
        List<GstTrackerDTO> dtoList = util.createGstTrackerDTO();
        wrapper.setGstDetails(dtoList);
        GstTrackerDTO trackerDTO;
        switch(gstReturnType.toUpperCase(Locale.ROOT)){
            case "GSTR1":
            case "GSTR3B":
                // Add return type and its filing details
                trackerDTO = createGstWrapperByReturnType(filedEntities,gstReturnType);
                dtoList.add(trackerDTO);
                break;
            case "BOTH":
                trackerDTO = createGstWrapperByReturnType(filedEntities,"GSTR1");
                trackerDTO.setFiledCount(
                        trackerDTO.getFilingDetails().stream().filter(obj-> obj.getIsFiled()==Boolean.TRUE).count()
                );
                trackerDTO.setNotFiledCount(
                        trackerDTO.getFilingDetails().stream().filter(obj-> obj.getIsFiled()==Boolean.FALSE).count()
                );
                dtoList.add(trackerDTO);
                trackerDTO = createGstWrapperByReturnType(filedEntities,"GSTR3B");
                trackerDTO.setFiledCount(
                        trackerDTO.getFilingDetails().stream().filter(obj-> obj.getIsFiled()==Boolean.TRUE).count()
                );
                trackerDTO.setNotFiledCount(
                        trackerDTO.getFilingDetails().stream().filter(obj-> obj.getIsFiled()==Boolean.FALSE).count()
                );
                dtoList.add(trackerDTO);
                break;
            default:
                break;
        }
        return wrapper;
    }

    public GstTrackerDTO createGstWrapperByReturnType(List<GstFiledEntity> filedEntities, String gstReturnType){
        List<GstFiledEntity> gstTypeList = filedEntities.stream()
                .filter(e -> e.getReturnType().equalsIgnoreCase(gstReturnType))
                .collect(Collectors.toList());
        GstTrackerDTO trackerDTO = util.createGstTrackerDto(gstReturnType);
        trackerDTO.setFilingDetails(util.createFiledAndNotFiledDetails(gstTypeList));
        return trackerDTO;
    }
}
