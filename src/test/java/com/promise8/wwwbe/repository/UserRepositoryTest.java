package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.v1.entity.UserEntityV1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("alpha")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 생성 테스트")
    void test() {
        UserEntityV1 user = userRepository.save(UserEntityV1.builder()
                .deviceId("test123")
                .userName("seongchan.kang22")
                .build());

        Assertions.assertEquals("test123", user.getDeviceId());
        Assertions.assertEquals("seongchan.kang22", user.getUserName());
    }

}