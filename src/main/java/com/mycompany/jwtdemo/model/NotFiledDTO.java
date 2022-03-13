package com.mycompany.jwtdemo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NotFiledDTO {
    private String gstNo;
    private String firmName;
    private String returnType;
    private Boolean isGstFiled;
    private LocalDate dateOfGstFiling;
}
