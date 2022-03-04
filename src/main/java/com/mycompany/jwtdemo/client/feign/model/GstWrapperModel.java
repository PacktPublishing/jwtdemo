package com.mycompany.jwtdemo.client.feign.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GstWrapperModel {

    private GstResponseModel data;
    private String status_cd;
    private String status_desc;
    private GstHeaderModel header;
}
