package com.mycompany.jwtdemo.controller;

import com.mycompany.jwtdemo.model.FilingOverviewDTO;
import com.mycompany.jwtdemo.model.NotFiledDTO;
import com.mycompany.jwtdemo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gst")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/get-reports/{caId}")
    public ResponseEntity<List<NotFiledDTO>> getReports(@RequestParam Integer fiscalYear, @RequestParam String month,
                                                        @RequestParam String returnType, @PathVariable Long caId){
        return ResponseEntity.ok(reportService.getReports(month, fiscalYear, returnType, caId));
    }
}
