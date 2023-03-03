package com.promise8.wwwbe.v1.repository;

import com.promise8.wwwbe.v1.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByDeviceId(String deviceId);

}
