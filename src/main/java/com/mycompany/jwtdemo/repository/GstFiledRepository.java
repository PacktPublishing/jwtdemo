package com.mycompany.jwtdemo.repository;

import com.mycompany.jwtdemo.entity.GstFiledEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface GstFiledRepository extends CrudRepository<GstFiledEntity,Long> {
    List<GstFiledEntity> findAllByGstNoAndReturnType(String gstNo, String returnType);
    List<GstFiledEntity> findAllByGstNoOrderByGstNo(String gstNo);
    List<GstFiledEntity> findAllByGstNoAndDateOfFilingBetween(String gstNo, LocalDate fromDate, LocalDate toDate);
    List<GstFiledEntity> findAllByGstNoAndReturnPeriodBetween(String gstNo, LocalDate fromDate, LocalDate toDate);
}
