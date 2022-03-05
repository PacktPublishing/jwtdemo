package com.mycompany.jwtdemo.controller;

import com.mycompany.jwtdemo.client.feign.GstFeignClient;
import com.mycompany.jwtdemo.client.feign.model.GstWrapperModel;
import com.mycompany.jwtdemo.model.GstAccountDTO;
import com.mycompany.jwtdemo.service.GstAccountService;
import com.mycompany.jwtdemo.service.GstApiCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gst")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GstAccountController {

    @Autowired
    private GstAccountService gstAccountService;

    @Autowired
    private GstApiCallService gstApiCallService;

    @PostMapping("/accounts")
    public ResponseEntity<GstAccountDTO> saveGstAccount(@RequestBody GstAccountDTO gstAccountDTO){
        GstAccountDTO gadto = gstAccountService.saveGstAccount(gstAccountDTO);
        return ResponseEntity.status(201).body(gadto);
    }

    @GetMapping("/accounts/{accountId}")
    public GstAccountDTO getAccountDetail(@PathVariable("accountId") Long accountId){
        GstAccountDTO gstAccountDTO = gstAccountService.getAccountDetail(accountId);
        return gstAccountDTO;
    }

    @DeleteMapping("/accounts/{accountId}")
    public GstAccountDTO deleteUpdateAccount(@PathVariable("accountId") Long accountId){
        GstAccountDTO gstAccountDTO = gstAccountService.deleteUpdateAccount(accountId);
        return gstAccountDTO;
    }

    @PutMapping("/accounts/{accountId}")
    public GstAccountDTO updateAccount(GstAccountDTO gstAccountDTO, @PathVariable("accountId") Long accountId){
        gstAccountDTO = gstAccountService.updateAccount(gstAccountDTO, accountId);
        return gstAccountDTO;
    }

    @GetMapping("/accounts/{caId}")
    public ResponseEntity<List<GstAccountDTO>>
    getAllMyGstAccounts(@PathVariable("caId") Long caId,
                        @RequestParam(defaultValue = "0") int pageNo,
                        @RequestParam(defaultValue = "3") int pageSize){

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<GstAccountDTO> accountDTOS = gstAccountService.getAllMyGstAccounts(caId, pageable);

        return ResponseEntity.ok(accountDTOS);
    }

    @GetMapping("/filings")
    public GstWrapperModel getAllFilings(){
        GstWrapperModel gfwm = gstApiCallService.getAllFilingsWithFeign("21ACJPT0060G1ZZ", "2020-21", "obify.consulting@gmail.com");
        //GstWrapperModel gfwm = gstFeignClient.getAllFilings("21ACJPT0060G1ZZ", "2020-21", "obify.consulting%40gmail.com");
        return gfwm;
    }
}
