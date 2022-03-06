package com.mycompany.jwtdemo.controller;

import com.mycompany.jwtdemo.model.GstTrackerWrapper;
import com.mycompany.jwtdemo.service.GstFiledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/gst")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GstController {

    @Autowired
    private GstFiledService service;

    @GetMapping("/all-gst-details")
    public ResponseEntity<GstTrackerWrapper> getAllDetailsForUser(@RequestParam String gstNo, @RequestParam String returnType,
                                                           @RequestParam("customFromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate customFromDate,
                                                           @RequestParam("customToDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate customToDate){
        return ResponseEntity.ok(service.getAllDetails(gstNo, customFromDate, customToDate, returnType));
    }
}
