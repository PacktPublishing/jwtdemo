package com.mycompany.jwtdemo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FilingOverviewDTO {

    private String firmName;
    private String gstNo;
    private String returnPeriodGstr1;
    private Map<String, Integer> gstr1NotFiledPeriod;
    private String returnPeriodGst3b;

}
