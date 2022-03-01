package com.mycompany.jwtdemo.controller;

import com.mycompany.jwtdemo.model.GstTrackerWrapper;
import com.mycompany.jwtdemo.service.GstFiledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gst")
@CrossOrigin
public class GstController {

    @Autowired
    private GstFiledService service;

    @GetMapping("/filed-details")
    public ResponseEntity<GstTrackerWrapper> getGstForUser(@RequestParam String gstNo, @RequestParam String returnType){
        return ResponseEntity.ok(service.getFiledDetailsByReturnType(gstNo, returnType));
    }

    @GetMapping("/unfiled-details")
    public ResponseEntity<HttpStatus> getNonFiledDetailsForUser(@RequestParam String gstNo, @RequestParam String fiscalYear){
        service.updateNonFilingDetails(fiscalYear, gstNo);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
