package com.promise8.wwwbe.service;

import com.promise8.wwwbe.repository.*;
import com.promise8.wwwbe.v1.model.dto.PromiseTime;
import com.promise8.wwwbe.v1.model.dto.req.MeetingCreateReqDtoV1;
import com.promise8.wwwbe.v1.model.dto.req.UserPromiseTimeReqDtoV1;
import com.promise8.wwwbe.v1.model.dto.res.ConfirmedPromiseResDtoV1;
import com.promise8.wwwbe.v1.model.dto.res.DynamicLinkResDtoV1;
import com.promise8.wwwbe.v1.model.entity.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith({MockitoExtension.class})
class MeetingServiceTest {
    private static final String TMP_ENDPOINT = "https://naver.com";
    private static MockedStatic<MeetingServiceHelper> meetingServiceHelper;
    private final String deviceId = "newDevice3451243";
    private final String userName = "user1";
    private final String meetingUserName = "meetingUser1";
    private final String meetingName = "meeting1";
    private final Long conditionCount = 6L;
    private final LocalDate startDate = LocalDate.of(2023, 2, 1);
    private final LocalDate endDate = LocalDate.of(2023, 3, 1);
    private final String meetingCode = "nEwcOD";
    private final String meetingPlace = "서울";
    @InjectMocks
    private MeetingService meetingService;
    @Mock
    private LinkService linkService;
    @Mock
    private PushService pushService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private MeetingUserRepository meetingUserRepository;
    @Mock
    private MeetingUserTimetableRepository meetingUserTimetableRepository;
    @Mock
    private MeetingPlaceRepository meetingPlaceRepository;
    private UserEntityV1 userEntity;
    private MeetingEntityV1 meetingEntity;
    private MeetingUserEntityV1 meetingUserEntity;
    private List<MeetingUserTimetableEntityV1> meetingUserTimetableEntityList = new ArrayList<>();
    private List<MeetingPlaceEntityV1> meetingPlaceEntityList = new ArrayList<>();
    private List<PlaceVoteEntityV1> placeVoteEntityList = new ArrayList<>();
    private MeetingCreateReqDtoV1 meetingCreateReqDto;
    private DynamicLinkResDtoV1 dynamicLinkResDto = new DynamicLinkResDtoV1();
    private ConfirmedPromiseResDtoV1 confirmedPromiseResDto = new ConfirmedPromiseResDtoV1();

    @BeforeEach
    public void init() {

        List<UserPromiseTimeReqDtoV1> promiseDateTimeReqDtoList = new ArrayList<>();
        List<String> promisePlaceList = new ArrayList<>();
        LocalDate promiseDate = LocalDate.now();
        PromiseTime promiseTime = PromiseTime.MORNING;
        promiseDateTimeReqDtoList.add(new UserPromiseTimeReqDtoV1(promiseDate, promiseTime));
        promisePlaceList.add(meetingPlace);

        userEntity = UserEntityV1.builder()
                .userId(55L)
                .deviceId(deviceId)
                .userName(userName)
                .isAlarmOn(true)
                .build();

        meetingEntity = MeetingEntityV1.builder()
                .meetingId(55L)
                .meetingName(meetingName)
                .conditionCount(conditionCount)
                .startDate(startDate)
                .endDate(endDate)
                .meetingCode(meetingCode)
                .meetingStatus(MeetingStatusV1.WAITING)
                .creator(userEntity)
                .build();

        meetingUserEntity = MeetingUserEntityV1.builder()
                .meetingUserName(meetingUserName)
                .userEntity(userEntity)
                .meetingEntity(meetingEntity)
                .build();

        meetingPlaceEntityList = List.of(MeetingPlaceEntityV1.builder()
                .promisePlace(meetingPlace)
                .isConfirmed(false)
                .meetingUserEntity(meetingUserEntity)
                .build());

        meetingUserTimetableEntityList = List.of(MeetingUserTimetableEntityV1.builder()
                .promiseDate(LocalDate.now())
                .promiseTime(PromiseTime.MORNING)
                .isConfirmed(false)
                .meetingUserEntity(meetingUserEntity)
                .build());

        meetingCreateReqDto = MeetingCreateReqDtoV1.builder()
                .meetingName(meetingName)
                .userName(userName)
                .minimumAlertMembers(conditionCount)
                .startDate(startDate)
                .endDate(endDate)
                .promiseDateTimeList(promiseDateTimeReqDtoList)
                .promisePlaceList(promisePlaceList)
                .build();

        placeVoteEntityList = List.of(PlaceVoteEntityV1.builder()
                .meetingUserEntity(meetingUserEntity)
                .meetingPlaceEntity(meetingPlaceEntityList.get(0))
                .build());

        meetingEntity.setMeetingUserEntityList(List.of(meetingUserEntity));
        meetingPlaceEntityList.get(0).setPlaceVoteEntityList(placeVoteEntityList);
        meetingUserEntity.setMeetingUserTimetableEntityList(meetingUserTimetableEntityList);
        meetingUserEntity.setMeetingPlaceEntityList(meetingPlaceEntityList);
        meetingUserEntity.setPlaceVoteEntityList(placeVoteEntityList);

        meetingServiceHelper = mockStatic(MeetingServiceHelper.class);
    }


    @AfterEach
    public void clear() {
        meetingServiceHelper.close();
    }

    @Test
    @DisplayName("meetingStatus 변경 확인")
    void putMeetingStatusWhenActionEndVote() {
        // when
        when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meetingEntity));
//        when(pushService.send(any(), any())).thenReturn(anyString());
        meetingService.putMeetingStatus(1L, MeetingStatusV1.VOTED);

        // then
        Assertions.assertEquals(MeetingStatusV1.VOTED, meetingEntity.getMeetingStatus());
    }

    @Test
    @DisplayName("createMeeting 테스트")
    void createMeetingTest() {
        given(meetingRepository.save(any())).willReturn(meetingEntity);
        given(meetingUserRepository.save(any())).willReturn(meetingUserEntity);
        given(meetingUserTimetableRepository.saveAll(any())).willReturn(meetingUserTimetableEntityList);
        given(meetingPlaceRepository.saveAll(any())).willReturn(meetingPlaceEntityList);
        when(linkService.createLink(any())).thenReturn(dynamicLinkResDto);
        meetingService.createMeeting(meetingCreateReqDto, deviceId);

        verify(linkService).createLink(any());
    }

    @Test
    @DisplayName("getMeetingById 테스트")
    void getMeetingById() {
        when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meetingEntity));
        meetingServiceHelper.when(() -> MeetingServiceHelper.getConfirmedPromise(meetingEntity.getMeetingUserEntityList(), userEntity.getUserId())).thenReturn(confirmedPromiseResDto);
        meetingServiceHelper.when(() -> MeetingServiceHelper.getHostAndVotingCnt(meetingEntity.getMeetingUserEntityList(), userEntity.getUserId())).thenReturn(confirmedPromiseResDto);
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(userEntity));

        meetingService.getMeetingById(meetingEntity.getMeetingId(), meetingEntity.getCreator().getUserId());
        meetingEntity.setMeetingStatus(MeetingStatusV1.DONE);
        meetingService.getMeetingById(meetingEntity.getMeetingId(), meetingEntity.getCreator().getUserId());

        verify(meetingRepository, times(2)).findById(anyLong());
        meetingServiceHelper.verify(() -> MeetingServiceHelper.getConfirmedPromise(any(), anyLong()), times(1));
        meetingServiceHelper.verify(() -> MeetingServiceHelper.getHostAndVotingCnt(any(), anyLong()), times(1));
    }

    @Test
    @DisplayName("getMeetingByMeetingCode 테스트")
    void getMeetingByMeetingCode() {
        when(meetingRepository.findByMeetingCode(anyString())).thenReturn(Optional.of(meetingEntity));
        meetingServiceHelper.when(() -> MeetingServiceHelper.getConfirmedPromise(meetingEntity.getMeetingUserEntityList(), userEntity.getUserId())).thenReturn(confirmedPromiseResDto);
        meetingServiceHelper.when(() -> MeetingServiceHelper.getHostAndVotingCnt(meetingEntity.getMeetingUserEntityList(), userEntity.getUserId())).thenReturn(confirmedPromiseResDto);
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(userEntity));

        meetingService.getMeetingByCode(anyString(), meetingEntity.getCreator().getUserId());
        meetingEntity.setMeetingStatus(MeetingStatusV1.DONE);
        meetingService.getMeetingByCode(anyString(), meetingEntity.getCreator().getUserId());

        verify(meetingRepository, times(2)).findByMeetingCode(anyString());
        meetingServiceHelper.verify(() -> MeetingServiceHelper.getConfirmedPromise(any(), anyLong()), times(1));
        meetingServiceHelper.verify(() -> MeetingServiceHelper.getHostAndVotingCnt(any(), anyLong()), times(1));
    }

    @Test
    @DisplayName("getMeetingList 테스트")
    void getMeetingListByDeviceId() {
        when(meetingRepository.findByUserEntity_DeviceId(anyString())).thenReturn(List.of(meetingEntity));
        meetingServiceHelper.when(() -> MeetingServiceHelper.getConfirmedPromise(meetingEntity.getMeetingUserEntityList(), userEntity.getUserId())).thenReturn(confirmedPromiseResDto);
        meetingServiceHelper.when(() -> MeetingServiceHelper.getHostAndVotingCnt(meetingEntity.getMeetingUserEntityList(), userEntity.getUserId())).thenReturn(confirmedPromiseResDto);

        meetingService.getMeetingListByDeviceId(deviceId);

        verify(meetingRepository).findByUserEntity_DeviceId(anyString());
        meetingServiceHelper.verify(() -> MeetingServiceHelper.getConfirmedPromise(any(), anyLong()), never());
        meetingServiceHelper.verify(() -> MeetingServiceHelper.getHostAndVotingCnt(any(), anyLong()), atLeastOnce());
    }
}
