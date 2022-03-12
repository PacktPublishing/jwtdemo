package com.mycompany.jwtdemo.service;

import com.mycompany.jwtdemo.client.feign.GstFeignClient;
import com.mycompany.jwtdemo.client.feign.model.GstDataModel;
import com.mycompany.jwtdemo.client.feign.model.GstWrapperModel;
import com.mycompany.jwtdemo.entity.GstFiledEntity;
import com.mycompany.jwtdemo.repository.GstFiledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class GstApiCallService {

    @Value("${gst.api.baseurl}")
    private String baseUrl;

    @Value("${gst.client.id}")
    private String clientId;

    @Value("${gst.client.sec}")
    private String clientSec;

    @Autowired
    private GstFiledRepository filedRepository;

    @Autowired
    private GstFeignClient gstFeignClient;

    @Autowired
    private RestTemplate restTemplate;

    public GstWrapperModel getAllFilings(String gstNo, String fy, String email){

        GstWrapperModel gstWrapperModel = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("client_id", clientId);
        headers.set("client_secret", clientSec);

        HttpEntity<GstWrapperModel> httpEntity = new HttpEntity<>(gstWrapperModel, headers);
        ResponseEntity<GstWrapperModel> gstEntity = restTemplate.exchange(baseUrl+"/rettrack-success.json?"+"gstin="+gstNo+"&fy="+fy+"&email=obify.consulting@gmail.com", HttpMethod.GET, httpEntity, GstWrapperModel.class);
        //ResponseEntity<GstWrapperModel> gstEntity = restTemplate.exchange(baseUrl+"/rettrack-success.json?"+"gstin="+gstNo+"&fy="+fy+"&email=obify.consulting@gmail.com", HttpMethod.GET, httpEntity, GstWrapperModel.class);

        if(gstEntity.getStatusCode() == HttpStatus.OK){
            gstWrapperModel = gstEntity.getBody();
        }

        return gstWrapperModel;
    }

    public GstWrapperModel getAllFilingsWithFeign(String gstNo, String fy, String email) {

        GstWrapperModel gfwm = null;
        try {
            //making actual Govt. GST API call
            gfwm = gstFeignClient.getAllFilings(gstNo, fy, email);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if(gfwm != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            //Converting every GST Filing entry for 1 GST Number for 1 FY into GstEntity and saving in Database
            if(gfwm.getData() != null) {
                for (GstDataModel gdm : gfwm.getData().getEFiledlist()) {
                    GstFiledEntity gstFiledEntity = new GstFiledEntity();
                    gstFiledEntity.setReturnType(gdm.getRtntype());
                    gstFiledEntity.setGstNo(gfwm.getHeader().getGstin());
                    LocalDate localDate = LocalDate.parse(gdm.getDof(), formatter);
                    gstFiledEntity.setDateOfFiling(localDate);
                    gstFiledEntity.setReturnPeriod(convertReturnPeriodDate(gdm.getRet_prd()));
                    gstFiledEntity.setStatus(gdm.getStatus());
                    gstFiledEntity.setMode(gdm.getMof());
                    filedRepository.save(gstFiledEntity);
                }
                System.out.println("Done for gstNo: "+gstNo);
            }
        }
        //log after successful insertion of all records for 1 GST number
        return gfwm;
    }

    private LocalDate convertReturnPeriodDate(String returnPeriod){
        return LocalDate
                .of(Integer.parseInt(returnPeriod.substring(2)),
                        Integer.parseInt(returnPeriod.substring(0,2)),
                        1 );
    }
}
