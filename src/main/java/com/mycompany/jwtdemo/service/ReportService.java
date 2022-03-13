package com.mycompany.jwtdemo.service;

import com.mycompany.jwtdemo.entity.GstAccountEntity;
import com.mycompany.jwtdemo.entity.NotFiledOverviewEntity;
import com.mycompany.jwtdemo.model.NotFiledDTO;
import com.mycompany.jwtdemo.repository.GstAccountRepository;
import com.mycompany.jwtdemo.repository.GstNotFiledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ReportService {

    @Autowired
    private GstNotFiledRepository notFiledRepository;

    @Autowired
    private GstAccountRepository accountRepository;

    public List<NotFiledDTO> getReports(String month, Integer year, String retType , Long caId) {
        LocalDate startDate, endDate;
        if(ObjectUtils.isEmpty(month)){
            startDate = LocalDate.of(year, Month.APRIL,1);
            endDate = LocalDate.of(year, Month.MARCH,31);
        }else {
            startDate = LocalDate.of(year, Month.valueOf(month.toUpperCase(Locale.ROOT)),1);
            endDate = LocalDate.of(year, Month.valueOf(month.toUpperCase(Locale.ROOT)),1)
                    .with(TemporalAdjusters.lastDayOfMonth());
        }
        List<GstAccountEntity> accounts =  getGstAccounts(caId);
        List<NotFiledDTO> notFiledList = new ArrayList<>();
        accounts.forEach(acct -> {
            List<NotFiledOverviewEntity> entityList = getReportsByReturnTypeAndGstNo(acct.getGstNo(), startDate, endDate, retType);
            convertEntityToDto(entityList, notFiledList);
        });
        return notFiledList;
    }

    private List<NotFiledDTO> convertEntityToDto(List<NotFiledOverviewEntity> entityList, List<NotFiledDTO> notFiledList) {
        entityList.forEach(e -> {
            NotFiledDTO nfd = new NotFiledDTO();
            nfd.setReturnType(e.getReturnType());
            nfd.setIsGstFiled(e.getIsGstFiled());
            nfd.setDateOfGstFiling(e.getDateOfGstFiling());
            nfd.setGstNo(e.getGstNo());
            GstAccountEntity gstAccount = accountRepository.findByGstNo(e.getGstNo()).get();
            nfd.setFirmName(gstAccount.getFirmName());
            notFiledList.add(nfd);
        });
        return notFiledList;
    }

    private List<NotFiledOverviewEntity> getReportsByReturnTypeAndGstNo(String gstNo, LocalDate startDate, LocalDate endDate, String retType) {
        List<NotFiledOverviewEntity> entityList;
        if(retType.equalsIgnoreCase("BOTH")) {
            entityList = notFiledRepository.findByGstNoAndReturnTypeAndDateOfGstFilingBetween(gstNo, "GSTR1", startDate, endDate);
            entityList.addAll(notFiledRepository.findByGstNoAndReturnTypeAndDateOfGstFilingBetween(gstNo, "GSTR3B", startDate, endDate));
        }else{
            entityList = notFiledRepository.findByGstNoAndReturnTypeAndDateOfGstFilingBetween(gstNo, retType, startDate, endDate);
        }
        return entityList;
    }

    public List<GstAccountEntity> getGstAccounts(Long caId){
        Pageable pageable = PageRequest.of(0, 100000);
        Page<GstAccountEntity> pagedAccounts = accountRepository.findByCaIdAndActiveContains(caId, "Y", pageable);

        return pagedAccounts.getContent();
    }
}
