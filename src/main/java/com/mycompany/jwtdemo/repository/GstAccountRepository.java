package com.mycompany.jwtdemo.repository;

import com.mycompany.jwtdemo.entity.GstAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GstAccountRepository extends JpaRepository<GstAccountEntity, Long> {

    Page<GstAccountEntity> findByCaId(Long caId, Pageable pageable);
}
