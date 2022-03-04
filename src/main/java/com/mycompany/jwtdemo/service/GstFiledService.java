package com.mycompany.jwtdemo.service;

import com.mycompany.jwtdemo.entity.GstAccountEntity;
import com.mycompany.jwtdemo.entity.GstFiledEntity;
import com.mycompany.jwtdemo.entity.GstNotFiledEntity;
import com.mycompany.jwtdemo.model.GstTrackerDTO;
import com.mycompany.jwtdemo.model.GstTrackerDetail;
import com.mycompany.jwtdemo.model.GstTrackerWrapper;
import com.mycompany.jwtdemo.repository.GstAccountRepository;
import com.mycompany.jwtdemo.repository.GstFiledRepository;
import com.mycompany.jwtdemo.repository.GstNotFiledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
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
    private GstNotFiledRepository notFiledRepository;

    public GstTrackerWrapper getFiledDetailsByReturnType(String gstNo, String returnType){
        GstTrackerWrapper wrapper = new GstTrackerWrapper();
        List<GstTrackerDTO> trackerList = new ArrayList<>();
        wrapper.setGstNo(gstNo);

        switch (returnType.toUpperCase(Locale.ROOT)){
            case "GSTR1":
            case "GSTR3B":
                trackerList.add(createGstTrackerDTO(gstNo,returnType));
                break;
            case "BOTH":
                trackerList.add(createGstTrackerDTO(gstNo, "GSTR1"));
                trackerList.add(createGstTrackerDTO(gstNo, "GSTR3B"));
            default:
                break;
        }
        wrapper.setGstDetails(trackerList);
        return wrapper;
    }

    private GstTrackerDTO createGstTrackerDTO(String gstNo,String returnType){
        GstTrackerDTO trackerDTO = new GstTrackerDTO();
        trackerDTO.setReturnType(returnType);
        trackerDTO.setFilingDetails(getFilingDetails(gstNo, returnType));
        return trackerDTO;
    }

    private List<GstTrackerDetail> getFilingDetails(String gstNo, String returnType){
        List<GstTrackerDetail> filingDetails = new ArrayList<>();
        List<GstFiledEntity> gstFiledEntities = filedRepository.findAllByGstNoAndReturnType(gstNo, returnType);
        //Add the details like date, month, year
        gstFiledEntities.forEach( entity -> {
            GstTrackerDetail detail = new GstTrackerDetail();
            detail.setDateOfFiling(entity.getDateOfFiling());
            detail.setMonth(entity.getDateOfFiling().getMonth().name());
            detail.setYear(entity.getDateOfFiling().getYear());
            detail.setIsFiled(Boolean.TRUE);
            filingDetails.add(detail);
        });
        return filingDetails;
    }

    public void updateNonFilingDetails(String fiscalYear,String gstNo){
        Year year = Year.of(Integer.parseInt(fiscalYear));
        List<GstFiledEntity> filedEntities = filedRepository.findAllByGstNoOrderByGstNo(gstNo);
        List<GstFiledEntity> filedEntities1 = filedEntities.stream()
                .filter(gstFiledEntity -> gstFiledEntity.getReturnType().equalsIgnoreCase("GSTR1"))
                .collect(Collectors.toList());
        createNonFiledGst(filedEntities1, year, gstNo, "GSTR1");
        filedEntities1 = filedEntities.stream()
                .filter(gstFiledEntity -> gstFiledEntity.getReturnType().equalsIgnoreCase("GSTR3B"))
                .collect(Collectors.toList());
        createNonFiledGst(filedEntities1, year, gstNo, "GSTR3B");

    }

    private void createNonFiledGst(List<GstFiledEntity> filedEntitiesWithReturnType, Year year, String gstNo, String returnType){
        List<GstNotFiledEntity> notFiledEntities = new ArrayList<>();
        getMonths().forEach(month -> {
            GstNotFiledEntity gnfe = new GstNotFiledEntity();
            gnfe.setGstNo(gstNo);
            gnfe.setReturnType(returnType);
            if(Month.JANUARY.toString().equalsIgnoreCase(month) || Month.FEBRUARY.toString().equalsIgnoreCase(month)
                    || Month.MARCH.toString().equalsIgnoreCase(month)) {
                gnfe.setNotFiled(LocalDate.of(year.plusYears(1).getValue(), Month.valueOf(month.toUpperCase(Locale.ROOT)), 1));
            }else{
                gnfe.setNotFiled(LocalDate.of(year.getValue(), Month.valueOf(month.toUpperCase(Locale.ROOT)), 1));
            }
            notFiledEntities.add(gnfe);
        });
        filedEntitiesWithReturnType.forEach(gstFiledEntity -> notFiledEntities.removeIf(gstNotFiledEntity ->
                gstNotFiledEntity.getNotFiled().getMonth() == gstFiledEntity.getDateOfFiling().getMonth()));
        notFiledRepository.saveAll(notFiledEntities);
    }

    private List<String> getMonths(){
        String[] monthsList = new DateFormatSymbols().getMonths();
        List<String> months = Arrays.stream(monthsList).collect(Collectors.toList());
        months.removeIf(String::isBlank);
        return months;
    }

    public String getFinancialYear(){
        LocalDate ld = LocalDate.now();
        Integer currentYear = ld.getYear();
        Integer prevYear = currentYear - 1;
        String fy = prevYear+"-"+currentYear.toString().substring(2);
        return fy;
    }

    //https://stackoverflow.com/questions/7979165/spring-cron-expression-for-every-after-30-minutes
    //@Scheduled(cron = "0 0/10 * * * ?")//every 10 min
    public void scheduleGetFilings(){
        System.out.println("*******Scheduler Started***********");
        //Delete all rows first than insert
        //filedRepository.deleteAll();
        //Read all GST number from GstAccount table
        List<GstAccountEntity> allGstAccEntities = gstAccountRepository.findAll();
        for(GstAccountEntity gae: allGstAccEntities) {
            gstApiCallService.getAllFilingsWithFeign(gae.getGstNo(), getFinancialYear(), "obify.consulting@gmail.com");
        }
        System.out.println("*******Scheduler Iteration Ended***********");
    }
}
