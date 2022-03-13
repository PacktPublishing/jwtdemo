package com.mycompany.jwtdemo.service;

import com.mycompany.jwtdemo.entity.GstAccountEntity;
import com.mycompany.jwtdemo.repository.GstAccountRepository;
import com.mycompany.jwtdemo.repository.GstFiledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GstMasterDataService {

    @Autowired
    private GstFiledRepository filedRepository;
    @Autowired
    private GstApiCallService gstApiCallService;
    @Autowired
    private GstAccountRepository gstAccountRepository;
    @Autowired
    private FilingOverviewService overviewService;

    public String getFinancialYear(String fyType){
        LocalDate ld = LocalDate.now();
        Integer currentYear = ld.getYear();
        Integer prevYear = currentYear - 1;
        if(fyType.equalsIgnoreCase("previous")){
            prevYear = prevYear-1;
            currentYear = currentYear-1;
        }
        return prevYear+"-"+currentYear.toString().substring(2);
    }

    //https://stackoverflow.com/questions/7979165/spring-cron-expression-for-every-after-30-minutes
    //@Scheduled(cron = "0 0/5 * * * ?")//every 5 min
    public void scheduleGetFilings(){
        System.out.println("*******Scheduler Started***********");
        //Delete all rows first than insert
        filedRepository.deleteAll();
        //Read all GST number from GstAccount table
        List<GstAccountEntity> allGstAccEntities = gstAccountRepository.findAll();
        performBatch(allGstAccEntities);
        overviewService.updateNotFiledOverview(allGstAccEntities);
        System.out.println("*******Scheduler Iteration Ended***********");
    }

    public void performBatch(List<GstAccountEntity> allGstAccEntities){
        for(GstAccountEntity gae: allGstAccEntities) {
            gstApiCallService.getAllFilingsWithFeign(gae.getGstNo(), getFinancialYear("current"), "obify.consulting@gmail.com");
        }
        for(GstAccountEntity gae: allGstAccEntities) {
            gstApiCallService.getAllFilingsWithFeign(gae.getGstNo(), getFinancialYear("previous"), "obify.consulting@gmail.com");
        }
    }
}
