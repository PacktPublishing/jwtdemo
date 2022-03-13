package com.mycompany.jwtdemo.controller;

import com.mycompany.jwtdemo.entity.GstAccountEntity;
import com.mycompany.jwtdemo.model.FilingOverviewDTO;
import com.mycompany.jwtdemo.service.FilingOverviewService;
import com.mycompany.jwtdemo.service.GstApiCallService;
import com.mycompany.jwtdemo.service.GstMasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gst")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FilingOverviewController {

    @Autowired
    private FilingOverviewService overviewService;

    @Autowired
    private GstMasterDataService gstMasterDataService;

    @GetMapping(value = "get-filing-det/{caId}")
    public ResponseEntity<List<FilingOverviewDTO>> getFilingOverview(@PathVariable Long caId, @RequestParam String fy){
        return ResponseEntity.ok(overviewService.getFilingOverview(caId, fy));
    }

    @GetMapping("/refresh-master-data/{caId}")
    public void refreshMasterData(@PathVariable Long caId){
       List<GstAccountEntity> gstAccountEntityList =  overviewService.getGstAccounts(caId);
       gstMasterDataService.performBatch(gstAccountEntityList);
       overviewService.updateNotFiledOverview(gstAccountEntityList);
    }
}
