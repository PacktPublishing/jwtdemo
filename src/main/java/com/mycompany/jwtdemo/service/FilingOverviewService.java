package com.mycompany.jwtdemo.service;

import com.mycompany.jwtdemo.entity.GstAccountEntity;
import com.mycompany.jwtdemo.entity.GstFiledEntity;
import com.mycompany.jwtdemo.model.FilingOverviewDTO;
import com.mycompany.jwtdemo.repository.GstAccountRepository;
import com.mycompany.jwtdemo.repository.GstFiledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilingOverviewService {

    @Autowired
    private GstAccountRepository accountRepository;

    @Autowired
    private GstFiledRepository gstFiledRepository;

    public List<FilingOverviewDTO> getFilingOverview(Long caId, String fy){
        List<FilingOverviewDTO> overviewDTOS = new ArrayList<>();
        Map<String,LocalDate> fiscalYear = calculateFiscalYear(fy);
        // Get the firm name and gst no
        List<GstAccountEntity> gstAccounts = getGstAccounts(caId);
        gstAccounts.stream().forEach(account -> {
            FilingOverviewDTO overviewDTO = new FilingOverviewDTO();
            overviewDTO.setFirmName(account.getFirmName());
            overviewDTO.setGstNo(account.getGstNo());
            overviewDTOS.add(overviewDTO);
        });

        overviewDTOS.stream().forEach(firm -> {
            List<GstFiledEntity> filedEntities = gstFiledRepository.
                    findAllByGstNoAndReturnPeriodBetween(firm.getGstNo(), fiscalYear.get("startDate"), fiscalYear.get("endDate"));
            firm.setReturnPeriodGst3b(getMostRecentReturnDateGstrByReturnType(filedEntities, "GSTR3B").toString());
            firm.setReturnPeriodGstr1(getMostRecentReturnDateGstrByReturnType(filedEntities, "GSTR1").toString());
            firm.setGstr1NotFiledPeriod(getNotFiledGstr1Months(filedEntities, fiscalYear));
        });
        return overviewDTOS;
    }

    private List<GstAccountEntity> getGstAccounts(Long caId){
        Pageable pageable = PageRequest.of(0, 100000);
        Page<GstAccountEntity> pagedAccounts = accountRepository.findByCaIdAndActiveContains(caId, "Y", pageable);

        List<GstAccountEntity> gstAccountEntities = pagedAccounts.getContent();
        return gstAccountEntities;
    }

    private LocalDate getMostRecentReturnDateGstrByReturnType(List<GstFiledEntity> filedEntities, String returnType){
        List<GstFiledEntity> gstr3BList = filedEntities.stream().filter(e -> e.getReturnType().equalsIgnoreCase(returnType))
                .collect(Collectors.toList());
        LocalDate maxDate = Collections.max(gstr3BList.stream().map(GstFiledEntity::getReturnPeriod).collect(Collectors.toList()));
        return maxDate;
    }

    private Map<String,LocalDate> calculateFiscalYear(String fy){
        String[] yr = fy.split("_");
        Map<String,LocalDate> fiscalYear = new HashMap<>();
        Integer year = Integer.parseInt(yr[0]);
        fiscalYear.put("startDate", LocalDate.of(year, Month.APRIL,1));
        fiscalYear.put("endDate", LocalDate.of(year + 1, Month.MARCH,31));
        return fiscalYear;
    }

    private Map<String,Integer> getNotFiledGstr1Months(List<GstFiledEntity> filedEntities , Map<String, LocalDate> fy){
        Map<String, Integer> notFiledDetails = new HashMap<>();
        LocalDate start = fy.get("startDate");
        while(start.isBefore(fy.get("endDate"))){
            notFiledDetails.put(start.getMonth().name(), start.getYear());
            start = start.plusMonths(1);
        }
        filedEntities.stream().forEach(e -> {
            notFiledDetails.remove(e.getReturnPeriod().getMonth().name(), e.getReturnPeriod().getYear());
        });
        return notFiledDetails;
    }
}
