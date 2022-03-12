package com.mycompany.jwtdemo.controller;

import com.mycompany.jwtdemo.model.FilingOverviewDTO;
import com.mycompany.jwtdemo.service.FilingOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gst")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FilingOverviewController {

    @Autowired
    private FilingOverviewService overviewService;

    @GetMapping(value = "get-filing-det/{caId}")
    public ResponseEntity<List<FilingOverviewDTO>> getFilingOverview(@PathVariable Long caId, @RequestParam String fy){
        return ResponseEntity.ok(overviewService.getFilingOverview(caId, fy));
    }
}
