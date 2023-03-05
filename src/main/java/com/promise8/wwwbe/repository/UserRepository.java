package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.v1.entity.UserEntityV1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntityV1, Long> {

    Optional<UserEntityV1> findByDeviceId(String deviceId);

}
