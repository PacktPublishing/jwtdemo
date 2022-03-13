package com.mycompany.jwtdemo.repository;

import com.mycompany.jwtdemo.entity.GstAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GstAccountRepository extends JpaRepository<GstAccountEntity, Long> {

    //@Query("SELECT g FROM GstAccountEntity g WHERE g.caId = :caId AND g.active = :active")
    //Page<GstAccountEntity> findByCaIdAndActive(@Param("caId") Long caId, @Param("active") String active, Pageable pageable);
    Page<GstAccountEntity> findByCaIdAndActiveContains(Long caId, String active, Pageable pageable);
    Optional<GstAccountEntity> findByGstNo(String gstNo);
}
