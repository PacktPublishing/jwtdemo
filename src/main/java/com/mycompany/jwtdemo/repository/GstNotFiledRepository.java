package com.mycompany.jwtdemo.repository;

import com.mycompany.jwtdemo.entity.NotFiledOverviewEntity;
import org.springframework.data.repository.CrudRepository;

public interface GstNotFiledRepository extends CrudRepository<NotFiledOverviewEntity,Long> {
}
