package com.mycompany.jwtdemo.service;

import com.mycompany.jwtdemo.entity.GstFiledEntity;
import com.mycompany.jwtdemo.repository.GstFiledRepository;
import com.mycompany.jwtdemo.repository.GstNotFiledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotFiledService {
    @Autowired
    private GstFiledRepository filedRepository;
    @Autowired
    private GstNotFiledRepository notFiledRepository;

    public void createNotFiledEntity(){
        List<GstFiledEntity> filedEntities = (List<GstFiledEntity>) filedRepository.findAll();
    }
}
