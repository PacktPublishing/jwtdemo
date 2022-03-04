package com.mycompany.jwtdemo.client.feign.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GstHeaderModel {

    @JsonProperty("sec-ch-ua")
    private String secchua;

    private String client_id;
    @JsonProperty("sec-ch-ua-mobile")
    private String secchuamobile;

    private String client_secret;

    @JsonProperty("sec-ch-ua-platform")
    private String secchuaplatform;

    @JsonProperty("sec-fetch-site")
    private String secfetchsite;

    @JsonProperty("sec-fetch-mode")
    private String secfetchmode;

    @JsonProperty("sec-fetch-dest")
    private String secfetchdest;

    private String gstin;


}
