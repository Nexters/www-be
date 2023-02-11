package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.entity.UserEntity;
import com.promise8.wwwbe.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("alpha")
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    void setAlarm() {
        UserEntity newUser = userRepository.save(UserEntity.builder()
                .isAlarmOn(false)
                .build());

        Assertions.assertFalse(newUser.getIsAlarmOn());

        userService.setAlarm(newUser.getUserId(), true);

        UserEntity savedUser = userRepository.findById(newUser.getUserId()).get();
        Assertions.assertTrue(savedUser.getIsAlarmOn());
    }
}