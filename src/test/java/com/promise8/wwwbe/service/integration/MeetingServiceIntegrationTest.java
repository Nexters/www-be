package com.promise8.wwwbe.service.integration;

import com.promise8.wwwbe.model.dto.req.JoinMeetingReqDto;
import com.promise8.wwwbe.model.dto.PromiseTime;
import com.promise8.wwwbe.model.dto.req.MeetingConfirmDto;
import com.promise8.wwwbe.model.dto.req.UserPromiseTimeReqDto;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                .meetingUserEntityList(new ArrayList<>())
                .conditionCount(2L)
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

    @Test
    void test2() {
        MeetingEntity savedMeeting = meetingRepository.save(MeetingEntity.builder()
                .meetingName("meeting")
                .build());

        MeetingPlaceEntity savedMeetingPlace = meetingPlaceRepository.save(MeetingPlaceEntity.builder()
                .promisePlace("test")
                .build());

        MeetingUserTimetableEntity savedMeetingUserTimetable = meetingUserTimetableRepository.save(MeetingUserTimetableEntity.builder()
                .promiseDate(LocalDate.now())
                .promiseTime(PromiseTime.NIGHT)
                .build());

        meetingService.confirmMeeting(savedMeeting.getMeetingId(), new MeetingConfirmDto(
                savedMeetingPlace.getMeetingPlaceId(),
                savedMeetingUserTimetable.getMeetingUserTimetableId()));

        MeetingPlaceEntity meetingPlaceEntity = meetingPlaceRepository.findById(savedMeetingPlace.getMeetingPlaceId()).get();
        MeetingUserTimetableEntity meetingUserTimetableEntity = meetingUserTimetableRepository.findById(savedMeetingUserTimetable.getMeetingUserTimetableId()).get();

        Assertions.assertTrue(meetingPlaceEntity.getIsConfirmed());
        Assertions.assertTrue(meetingUserTimetableEntity.getIsConfirmed());
    }
    
    @Test
    public void test3() {
        MeetingEntity meeting = MeetingEntity.builder().build();
        meeting.setVoteFinishDateTime(LocalDateTime.now().minusDays(1));
        meeting.setMeetingStatus(MeetingStatus.VOTED);
        meetingRepository.save(meeting);

        List<MeetingEntity> voteNotiNeedMeetingList = meetingService.getVoteNotiNeedMeetingList();
        Assertions.assertTrue(voteNotiNeedMeetingList.size() >= 1);
    }

    private UserPromiseTimeReqDto createPromise(LocalDate date, PromiseTime promiseTime) {
        return UserPromiseTimeReqDto.builder()
                .promiseDate(date)
                .promiseTime(promiseTime)
                .build();
    }
}
