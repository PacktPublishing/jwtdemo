package com.mycompany.jwtdemo.controller;

import com.mycompany.jwtdemo.model.GstAccountDTO;
import com.mycompany.jwtdemo.service.GstAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gst")
@CrossOrigin
public class GstAccountController {

    @Autowired
    private GstAccountService gstAccountService;

    @PostMapping("/accounts")
    public ResponseEntity<GstAccountDTO> saveGstAccount(@RequestBody GstAccountDTO gstAccountDTO){
        GstAccountDTO gadto = gstAccountService.saveGstAccount(gstAccountDTO);
        return ResponseEntity.status(201).body(gadto);
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
}
