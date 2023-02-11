package com.promise8.wwwbe.service.integration;

import com.promise8.wwwbe.model.dto.JoinMeetingReqDto;
import com.promise8.wwwbe.model.dto.PromiseTime;
import com.promise8.wwwbe.model.dto.UserPromiseTimeReqDto;
import com.promise8.wwwbe.model.entity.*;
import com.promise8.wwwbe.repository.*;
import com.promise8.wwwbe.service.MeetingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("alpha")
@Transactional
public class MeetingServiceIntegrationTest {

    @Autowired
    MeetingService meetingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    MeetingUserRepository meetingUserRepository;

    @Autowired
    MeetingUserTimetableRepository meetingUserTimetableRepository;

    @Autowired
    MeetingPlaceRepository meetingPlaceRepository;

    @Test
    public void test() {

        //given

        // 방 생성자
        UserEntity host = userRepository.save(UserEntity.builder()
                .userName("host")
                .build());

        // meeting
        MeetingEntity meeting = meetingRepository.save(MeetingEntity.builder()
                .creator(host)
                .build());

        // 참여자
        UserEntity meetingUser = userRepository.save(UserEntity.builder()
                .userName("meetingUser")
                .build());


        // when
        LocalDate today = LocalDate.now();
        Long joinedMeetingId = meetingService.joinMeetingAndGetMeetingUserId(meetingUser.getUserId(), meeting.getMeetingId(),
                JoinMeetingReqDto.builder()
                .nickname("kohen.kang")
                .promisePlaceList(Arrays.asList("강남", "역삼", "홍대"))
                .userPromiseTimeList(Arrays.asList(
                        createPromise(today, PromiseTime.MORNING),
                        createPromise(today, PromiseTime.NIGHT)))
                .build());


        //then
        Optional<MeetingUserEntity> meetingUserEntityOptional = meetingUserRepository.findById(joinedMeetingId);

        Assertions.assertTrue(meetingUserEntityOptional.isPresent());

        MeetingUserEntity createdMeetingUserEntity = meetingUserEntityOptional.get();
        Assertions.assertEquals(meeting.getMeetingId(), createdMeetingUserEntity.getMeetingEntity().getMeetingId());

        List<MeetingPlaceEntity> createdMeetingPlaceEntityList =
                meetingPlaceRepository.findByMeetingUserEntity(createdMeetingUserEntity);
        Assertions.assertEquals(3, createdMeetingPlaceEntityList.size());

        List<MeetingUserTimetableEntity> createdMeetingUserTimetableEntityList = meetingUserTimetableRepository.findByMeetingUserEntity(createdMeetingUserEntity);
        Assertions.assertEquals(2, createdMeetingUserTimetableEntityList.size());
    }

    private UserPromiseTimeReqDto createPromise(LocalDate date, PromiseTime promiseTime) {
        return UserPromiseTimeReqDto.builder()
                .promiseDate(date)
                .promiseTime(promiseTime)
                .build();
    }
}
