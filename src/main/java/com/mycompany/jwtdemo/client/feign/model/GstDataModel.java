package com.mycompany.jwtdemo.client.feign.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GstDataModel {

    private String valid;
    private String mof;
    private String dof;
    private String rtntype;
    private String ret_prd;
    private String arn;
    private String status;

}
