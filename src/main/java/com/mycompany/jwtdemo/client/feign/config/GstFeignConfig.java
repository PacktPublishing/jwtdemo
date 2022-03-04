package com.mycompany.jwtdemo.client.feign.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GstFeignConfig {

    @Value("${gst.client.id}")
    private String clientId;

    @Value("${gst.client.sec}")
    private String clientSec;

    public GstFeignConfig(){}

    @Bean
    public RequestInterceptor requestInterceptor(){
        return (requestTemplate) -> {
            requestTemplate.header("client_id", clientId);
            requestTemplate.header("client_secret", clientSec);
        };
    }
    @Bean
    //@Profile({"!local && !dev && !test"})
    public ErrorDecoder errorDecoder(){

        return new GstCustomErrorDecoder();
    }


}
