package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.UserEntity;
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
        UserEntity user = userRepository.save(UserEntity.builder()
                .deviceId("test")
                .userName("seongchan.kang")
                .build());

        Assertions.assertEquals("test", user.getDeviceId());
        Assertions.assertEquals("seongchan.kang", user.getUserName());
    }

}