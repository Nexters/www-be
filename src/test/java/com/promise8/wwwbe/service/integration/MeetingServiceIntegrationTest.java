package com.promise8.wwwbe.service.integration;

import com.promise8.wwwbe.repository.*;
import com.promise8.wwwbe.service.MeetingService;
import com.promise8.wwwbe.v1.model.dto.PromiseTime;
import com.promise8.wwwbe.v1.model.dto.req.JoinMeetingReqDtoV1;
import com.promise8.wwwbe.v1.model.dto.req.MeetingConfirmDtoV1;
import com.promise8.wwwbe.v1.model.dto.req.UserPromiseTimeReqDtoV1;
import com.promise8.wwwbe.v1.model.entity.*;
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
        UserEntityV1 host = userRepository.save(UserEntityV1.builder()
                .userName("host")
                .build());

        // meeting
        MeetingEntityV1 meeting = meetingRepository.save(MeetingEntityV1.builder()
                .creator(host)
                .meetingUserEntityList(new ArrayList<>())
                .conditionCount(2L)
                .build());

        // 참여자
        UserEntityV1 meetingUser = userRepository.save(UserEntityV1.builder()
                .userName("meetingUser")
                .build());


        // when
        LocalDate today = LocalDate.now();
        Long joinedMeetingId = meetingService.joinMeetingAndGetMeetingUserId(meetingUser.getUserId(), meeting.getMeetingId(),
                JoinMeetingReqDtoV1.builder()
                        .nickname("kohen.kang")
                        .promisePlaceList(Arrays.asList("강남", "역삼", "홍대"))
                        .userPromiseTimeList(Arrays.asList(
                                createPromise(today, PromiseTime.MORNING),
                                createPromise(today, PromiseTime.NIGHT)))
                        .build());


        //then
        Optional<MeetingUserEntityV1> meetingUserEntityOptional = meetingUserRepository.findById(joinedMeetingId);

        Assertions.assertTrue(meetingUserEntityOptional.isPresent());

        MeetingUserEntityV1 createdMeetingUserEntity = meetingUserEntityOptional.get();
        Assertions.assertEquals(meeting.getMeetingId(), createdMeetingUserEntity.getMeetingEntity().getMeetingId());

        List<MeetingPlaceEntityV1> createdMeetingPlaceEntityList =
                meetingPlaceRepository.findByMeetingUserEntity(createdMeetingUserEntity);
        Assertions.assertEquals(3, createdMeetingPlaceEntityList.size());

        List<MeetingUserTimetableEntityV1> createdMeetingUserTimetableEntityList = meetingUserTimetableRepository.findByMeetingUserEntity(createdMeetingUserEntity);
        Assertions.assertEquals(2, createdMeetingUserTimetableEntityList.size());
    }

    @Test
    void test2() {
        MeetingEntityV1 savedMeeting = meetingRepository.save(MeetingEntityV1.builder()
                .meetingName("meeting")
                .build());

        MeetingPlaceEntityV1 savedMeetingPlace = meetingPlaceRepository.save(MeetingPlaceEntityV1.builder()
                .promisePlace("test")
                .build());

        MeetingUserTimetableEntityV1 savedMeetingUserTimetable = meetingUserTimetableRepository.save(MeetingUserTimetableEntityV1.builder()
                .promiseDate(LocalDate.now())
                .promiseTime(PromiseTime.NIGHT)
                .build());

        meetingService.confirmMeeting(savedMeeting.getMeetingId(), new MeetingConfirmDtoV1(
                savedMeetingPlace.getMeetingPlaceId(),
                savedMeetingUserTimetable.getMeetingUserTimetableId()));

        MeetingPlaceEntityV1 meetingPlaceEntity = meetingPlaceRepository.findById(savedMeetingPlace.getMeetingPlaceId()).get();
        MeetingUserTimetableEntityV1 meetingUserTimetableEntity = meetingUserTimetableRepository.findById(savedMeetingUserTimetable.getMeetingUserTimetableId()).get();

        Assertions.assertTrue(meetingPlaceEntity.getIsConfirmed());
        Assertions.assertTrue(meetingUserTimetableEntity.getIsConfirmed());
    }
    
    @Test
    public void test3() {
        MeetingEntityV1 meeting = MeetingEntityV1.builder().build();
        meeting.setVoteFinishDateTime(LocalDateTime.now().minusDays(1));
        meeting.setMeetingStatus(MeetingStatusV1.VOTED);
        meetingRepository.save(meeting);

        List<MeetingEntityV1> voteNotiNeedMeetingList = meetingService.getVoteNotiNeedMeetingList();
        Assertions.assertTrue(voteNotiNeedMeetingList.size() >= 1);
    }

    private UserPromiseTimeReqDtoV1 createPromise(LocalDate date, PromiseTime promiseTime) {
        return UserPromiseTimeReqDtoV1.builder()
                .promiseDate(date)
                .promiseTime(promiseTime)
                .build();
    }
}
