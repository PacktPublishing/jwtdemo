package com.mycompany.jwtdemo.repository;

import com.mycompany.jwtdemo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findByUsernameAndActiveContains(String username, String active);
}
