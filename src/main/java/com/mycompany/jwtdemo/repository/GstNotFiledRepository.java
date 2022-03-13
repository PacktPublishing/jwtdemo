package com.mycompany.jwtdemo.repository;

import com.mycompany.jwtdemo.entity.NotFiledOverviewEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface GstNotFiledRepository extends CrudRepository<NotFiledOverviewEntity,Long> {
    List<NotFiledOverviewEntity> findByGstNoAndReturnTypeAndDateOfGstFilingBetween(String gstNo,
                                                  String retType, LocalDate start, LocalDate end);
}
