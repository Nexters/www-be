package com.promise8.wwwbe.service;

import com.promise8.wwwbe.repository.UserRepository;
import com.promise8.wwwbe.v1.model.entity.UserEntityV1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
        UserEntityV1 newUser = userRepository.save(UserEntityV1.builder()
                .isAlarmOn(false)
                .build());

        Assertions.assertFalse(newUser.getIsAlarmOn());

        userService.setAlarm(newUser.getUserId(), true);

        UserEntityV1 savedUser = userRepository.findById(newUser.getUserId()).get();
        Assertions.assertTrue(savedUser.getIsAlarmOn());
    }
}