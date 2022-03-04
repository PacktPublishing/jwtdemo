package com.mycompany.jwtdemo.client.feign;

import com.mycompany.jwtdemo.client.feign.config.GstFeignConfig;
import com.mycompany.jwtdemo.client.feign.model.GstWrapperModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gstFeignClient", url = "${gst.api.baseurl:}", configuration = GstFeignConfig.class)
public interface GstFeignClient {

    @GetMapping("/public/rettrack")
    //@GetMapping("/rettrack-success.json")
    GstWrapperModel getAllFilings(@RequestParam("gstin") String gstNo, @RequestParam("fy") String fy, @RequestParam("email") String email);
}
