package com.mycompany.jwtdemo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class FilingOverviewDTO {

    private String firmName;
    private String gstNo;
    private LocalDate returnPeriodGstr1;
    private Map<String, Integer> gstr1NotFiledPeriod;
    private LocalDate returnPeriodGst3b;

}
