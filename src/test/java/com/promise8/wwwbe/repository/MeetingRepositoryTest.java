package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.dto.MeetingStatus;
import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("")
class MeetingRepositoryTest {
    private final String deviceId = "newDevice3451243";
    private final String userName = "user1";
    private final String meetingUserName = "meetingUser1";
    private final String meetingName = "meeting1";
    private final Long conditionCount = 6L;
    private final LocalDate startDate = LocalDate.of(2023, 2, 1);
    private final LocalDate endDate = LocalDate.of(2023, 3, 1);
    private final String meetingCode = "nEwcOD";
    private UserEntity userEntity;
    private MeetingEntity meetingEntity;
    private MeetingUserEntity meetingUserEntity;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private MeetingUserRepository meetingUserRepository;

    @BeforeEach
    void init() {
        userEntity = userRepository.save(UserEntity.builder()
                .deviceId(deviceId)
                .userName(userName)
                .build());
        meetingEntity = meetingRepository.save(MeetingEntity.builder()
                .meetingId(55L)
                .meetingName(meetingName)
                .conditionCount(conditionCount)
                .startDate(startDate)
                .endDate(endDate)
                .meetingCode(meetingCode)
                .meetingStatus(MeetingStatus.WAITING.name())
                .userEntity(userEntity)
                .build());
        meetingUserEntity = meetingUserRepository.save(MeetingUserEntity.builder()
                .meetingUserName(meetingUserName)
                .userEntity(userEntity)
                .meetingEntity(meetingEntity)
                .build());
    }

    @Test
    @DisplayName("약속방 생성 테스트")
    void createTest() {
        assertThat(meetingEntity.getMeetingName()).isEqualTo(meetingName);
        assertThat(meetingEntity.getConditionCount()).isEqualTo(conditionCount);
        assertThat(meetingEntity.getStartDate()).isEqualTo(startDate);
        assertThat(meetingEntity.getEndDate()).isEqualTo(endDate);
        assertThat(meetingEntity.getMeetingCode()).isEqualTo(meetingCode);
        assertThat(meetingEntity.getMeetingStatus()).isEqualTo(MeetingStatus.WAITING.name());
        assertThat(meetingEntity.getUserEntity().getUserId()).isEqualTo(userEntity.getUserId());
    }

    @Test
    @DisplayName("약속방 코드 존재여부 확인 테스트")
    void isExistMeetingCodeTest() {
        String existMeetingCode = meetingRepository.isExistMeetingCode(meetingCode);
        assertThat(existMeetingCode).isEqualTo(meetingCode);
    }

    @Test
    @DisplayName("약속방 코드로 약속방 조회 테스트")
    void findByMeetingCode() {
        MeetingEntity findMeetingEntity = meetingRepository.findByMeetingCode(meetingCode).orElseThrow();
        assertThat(findMeetingEntity.getMeetingCode()).isEqualTo(meetingCode);
    }

    @Test
    @DisplayName("유저가 참여한 약속방 리스트 조회 테스트")
    void findMeetingByDeviceId() {
        List<MeetingEntity> meetingEntityList = meetingRepository.findByUserEntity_DeviceId(deviceId);
        assertThat(meetingEntityList.size()).isEqualTo(1);
        assertThat(meetingEntityList.get(0).getUserEntity().getDeviceId()).isEqualTo(deviceId);
    }
}
