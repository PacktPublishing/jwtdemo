package com.mycompany.jwtdemo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "gst_account")
@Table(name = "gst_account")
public class GstAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gstNo;
    private String contactPerson;
    private String proprietorName;
    private String email;
    private String phone;
    private String gstPortalUsername;
    private String gstPortalPassword;
    private String firmName;
    private Long caId;
    private LocalDate creationDate;
    private String active;
}
