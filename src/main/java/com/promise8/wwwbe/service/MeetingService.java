package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.*;
import com.promise8.wwwbe.model.entity.*;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import com.promise8.wwwbe.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private static final int MEETING_CODE_LENGTH = 6;
    // TODO FIX ENDPOINT
    private static final String TMP_ENDPOINT = "https://naver.com";
    private final PushService pushService;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final MeetingUserTimetableRepository meetingUserTimetableRepository;
    private final MeetingPlaceRepository meetingPlaceRepository;
    private final PlaceVoteRepository placeVoteRepository;
    private final LinkService linkService;

    @Transactional
    public MeetingCreateResDto createMeeting(MeetingCreateReqDto meetingCreateReqDto, String deviceId) {
        String meetingCode = getMeetingCode();
        UserEntity userEntity = getUser(deviceId, meetingCreateReqDto.getUserName());
        DynamicLinkResDto dynamicLinkResDto = linkService.createLink(meetingCreateReqDto.getPlatformType(), TMP_ENDPOINT);
        String shortLink = null;
        if (dynamicLinkResDto != null) {
            shortLink = dynamicLinkResDto.getShortLink();
        }

        MeetingEntity meetingEntity = meetingRepository.save(meetingCreateReqDto.of(userEntity, meetingCode, shortLink));
        MeetingUserEntity meetingUserEntity = meetingUserRepository.save(MeetingUserEntity.builder()
                .meetingUserName(meetingCreateReqDto.getUserName())
                .userEntity(userEntity)
                .meetingEntity(meetingEntity)
                .build()
        );

        // TODO Refactoring
        List<MeetingUserTimetableEntity> meetingUserTimetableEntityList = new ArrayList<>();
        for (PromiseDateAndTimeReqDto promiseDateAndTimeReqDto : meetingCreateReqDto.getPromiseDateAndTimeReqDtoList()) {
            LocalDate promiseDate = promiseDateAndTimeReqDto.getPromiseDate();
            List<PromiseTime> promiseTimeList = promiseDateAndTimeReqDto.getPromiseTimeList();
            if (promiseTimeList == null || promiseTimeList.size() == 0) {
                // TODO Check promiseTime is possible null
                meetingUserTimetableEntityList.add(MeetingUserTimetableEntity.builder()
                        .promiseDate(promiseDate)
                        .promiseTime(null)
                        .meetingUserEntity(meetingUserEntity)
                        .build()
                );
            } else {
                for (PromiseTime promiseTime : promiseTimeList) {
                    meetingUserTimetableEntityList.add(MeetingUserTimetableEntity.builder()
                            .promiseDate(promiseDate)
                            .promiseTime(promiseTime)
                            .meetingUserEntity(meetingUserEntity)
                            .build()
                    );
                }
            }
        }
        meetingUserTimetableRepository.saveAll(meetingUserTimetableEntityList);

        // TODO Refactoring
        List<MeetingPlaceEntity> meetingPlaceEntityList = new ArrayList<>();
        for (String promisePlace : meetingCreateReqDto.getPromisePlaceList()) {
            meetingPlaceEntityList.add(MeetingPlaceEntity.builder()
                    .promisePlace(promisePlace)
                    .meetingUserEntity(meetingUserEntity)
                    .build());
        }
        meetingPlaceRepository.saveAll(meetingPlaceEntityList);

        return MeetingCreateResDto.of(meetingCode, shortLink);
    }

    @Transactional
    public void putMeetingStatus(long meetingId, MeetingStatus meetingStatus) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.INVALID_REQUEST, "not exist meeting");
        });

        if (MeetingStatus.WAITING.equals(meetingStatus) || MeetingStatus.DONE.equals(meetingStatus)) {
            throw new BizException(BaseErrorCode.INVALID_REQUEST, "not support action");
        }

        meetingEntity.setMeetingStatus(meetingStatus);
    }

    private String getMeetingCode() {
        String code = RandomStringUtils.random(MEETING_CODE_LENGTH, true, false);
        while (true) {
            String existMeetingCode = meetingRepository.isExistMeetingCode(code);
            if (existMeetingCode == null) {
                return code;
            }
        }
    }

    private UserEntity getUser(String deviceId, String userName) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByDeviceId(deviceId);

        UserEntity userEntity = null;
        if (optionalUserEntity.isPresent()) {
            userEntity = optionalUserEntity.get();
            userEntity.setUserName(userName);
            return userRepository.save(userEntity);
        } else {
            return userRepository.save(UserEntity.builder()
                    .deviceId(deviceId)
                    .userName(userName)
                    .build());
        }
    }

    public MeetingGetResDto getMeetingById(long meetingId, long currentUserId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING, "Meeting not exist with meetingId."));
        return getMeetingInfo(meetingEntity, currentUserId);
    }

    public MeetingGetResDto getMeetingByCode(String meetingCode, long currentUserId) {
        MeetingEntity meetingEntity = meetingRepository.findByMeetingCode(meetingCode)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING, "Meeting not exist with meetingCode."));
        return getMeetingInfo(meetingEntity, currentUserId);
    }

    private MeetingGetResDto getMeetingInfo(MeetingEntity meetingEntity, long currentUserId) {
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            throw new BizException(BaseErrorCode.SERVER_ERROR, "Server Error");
        }

        boolean isJoined = meetingEntity.getMeetingUserEntityList().stream()
                .anyMatch(meetingUserEntity -> meetingUserEntity.getUserEntity().getUserId() == currentUserId);

        if (!isJoined && !MeetingStatus.WAITING.equals(meetingEntity.getMeetingStatus())) {
            throw new BizException(BaseErrorCode.ALREADY_VOTING_MEETING, "Voting already start.");
        }

        ConfirmedPromiseDto confirmedPromiseDto = null;
        if (MeetingStatus.DONE.equals(meetingEntity.getMeetingStatus()) || MeetingStatus.CONFIRMED.equals(meetingEntity.getMeetingStatus())) {
            confirmedPromiseDto = MeetingServiceHelper.getConfirmedPromise(meetingEntity.getMeetingUserEntityList(), meetingEntity.getCreator().getUserId());
        } else {
            confirmedPromiseDto = MeetingServiceHelper.getHostAndVotingCnt(meetingEntity.getMeetingUserEntityList(), meetingEntity.getCreator().getUserId());
        }

        return MeetingGetResDto.of(
                meetingEntity,
                getUserPromisePlaceResDtoList(meetingEntity),
                getUserPromiseTimeList(meetingEntity),
                getUserVoteHashMap(meetingEntity),
                confirmedPromiseDto,
                currentUserId,
                isJoined
        );
    }

    public MeetingMainGetResDtoWrapper getMeetingListByDeviceId(String deviceId) {
        List<MeetingEntity> meetingEntityList = meetingRepository.findByUserEntity_DeviceId(deviceId);

        return MeetingMainGetResDtoWrapper.of(meetingEntityList);
    }

    public Long joinMeetingAndGetMeetingUserId(long userId, long meetingId, JoinMeetingReqDto joinMeetingReqDto) {
        Boolean isExistMeetingUser = meetingUserRepository.existsMeetingUserEntityByUserEntity_UserIdAndMeetingEntity_MeetingId(userId, meetingId);
        if (isExistMeetingUser) {
            throw new BizException(BaseErrorCode.ALREADY_PARTICIPATED_MEETING);
        }
        MeetingEntity meetingEntity =
                meetingRepository.findById(meetingId).orElseThrow(() -> new BizException(BaseErrorCode.INVALID_REQUEST, "not exist meeting"));
        UserEntity userEntity =
                userRepository.findById(userId).orElseThrow(() -> new BizException(BaseErrorCode.INVALID_REQUEST, "not exist user"));


        MeetingUserEntity meetingUserEntity = meetingUserRepository.save(MeetingUserEntity.builder()
                .userEntity(userEntity)
                .meetingEntity(meetingEntity)
                .meetingUserName(joinMeetingReqDto.getNickname())
                .build());


        List<MeetingUserTimetableEntity> meetingUserTimetableEntityList =
                joinMeetingReqDto.getUserPromiseTimeList().stream()
                        .map(meetingReq -> MeetingUserTimetableEntity.builder()
                                .meetingUserEntity(meetingUserEntity)
                                .promiseDate(meetingReq.getPromiseDate())
                                .promiseTime(meetingReq.getPromiseTime())
                                .isConfirmed(false)
                                .build()
                        )
                        .collect(Collectors.toList());

        meetingUserTimetableRepository.saveAll(meetingUserTimetableEntityList);

        List<MeetingPlaceEntity> meetingPlaceEntityList = joinMeetingReqDto.getPromisePlaceList().stream()
                .map(place -> meetingPlaceRepository.save(MeetingPlaceEntity.builder()
                        .meetingUserEntity(meetingUserEntity)
                        .promisePlace(place)
                        .isConfirmed(false)
                        .build()))
                .collect(Collectors.toList());

        meetingPlaceRepository.saveAll(meetingPlaceEntityList);
        return meetingUserEntity.getMeetingUserId();
    }

    private List<UserPromiseTimeResDto> getUserPromiseTimeList(MeetingEntity meetingEntity) {
        List<UserPromiseTimeResDto> userPromiseTimeResDtoList = new ArrayList<>();
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            return userPromiseTimeResDtoList;
        }

        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            meetingUser.getMeetingUserTimetableEntityList().forEach(res -> {
                LocalDate promiseDate = res.getPromiseDate();
                PromiseTime promiseTime = res.getPromiseTime();

                boolean isAdd = false;
                for (UserPromiseTimeResDto userPromiseTime : userPromiseTimeResDtoList) {
                    if (userPromiseTime.getPromiseDate().equals(promiseDate) && userPromiseTime.getPromiseTime().equals(promiseTime)) {
                        userPromiseTime.getUserNameList().add(meetingUser.getMeetingUserName());
                        isAdd = true;
                        break;
                    }
                }

                if (!isAdd) {
                    List<String> userNameList = new ArrayList<>();
                    userNameList.add(meetingUser.getMeetingUserName());
                    userPromiseTimeResDtoList.add(UserPromiseTimeResDto.builder()
                            .promiseDate(promiseDate)
                            .promiseTime(promiseTime)
                            .promiseDayOfWeek(getPromiseDayOfWeek(promiseDate))
                            .userNameList(userNameList)
                            .build());
                }
            });
        });

        Collections.sort(userPromiseTimeResDtoList, new Comparator<UserPromiseTimeResDto>() {
            @Override
            public int compare(UserPromiseTimeResDto o1, UserPromiseTimeResDto o2) {
                return o2.getUserNameList().size() - o1.getUserNameList().size();
            }
        });

        return userPromiseTimeResDtoList;
    }

    private PromiseDayOfWeek getPromiseDayOfWeek(LocalDate promiseDate) {
        DayOfWeek dayOfWeek = promiseDate.getDayOfWeek();
        int idx = dayOfWeek.getValue();
        return PromiseDayOfWeek.values()[idx - 1];
    }

    private HashMap<String, List<String>> getUserVoteHashMap(MeetingEntity meetingEntity) {
        HashMap<String, List<String>> userVoteHashMap = new HashMap<>();
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            return userVoteHashMap;
        }

        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            meetingUser.getMeetingPlaceEntityList().forEach(meetingPlace -> {
                meetingPlace.getPlaceVoteEntityList().forEach(res -> {
                    String promisePlace = res.getMeetingPlaceEntity().getPromisePlace();
                    if (userVoteHashMap.containsKey(promisePlace)) {
                        userVoteHashMap.get(promisePlace).add(res.getMeetingUserEntity().getMeetingUserName());
                    } else {
                        List<String> userNameList = new ArrayList<>();
                        userNameList.add(res.getMeetingUserEntity().getMeetingUserName());
                        userVoteHashMap.put(promisePlace, userNameList);
                    }
                });
            });
        });

        return userVoteHashMap;
    }

    private List<UserPromisePlaceResDto> getUserPromisePlaceResDtoList(MeetingEntity meetingEntity) {
        List<UserPromisePlaceResDto> userPromisePlaceResDtoList = new ArrayList<>();
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            return userPromisePlaceResDtoList;
        }

        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            userPromisePlaceResDtoList.addAll(meetingUser.getMeetingPlaceEntityList().stream()
                    .map(UserPromisePlaceResDto::of).collect(Collectors.toList()));
        });

        return userPromisePlaceResDtoList;
    }

    @Scheduled(cron = "2 0 0 * * ?", zone = "Asia/Seoul")
    public void promiseDone() {
        List<MeetingEntity> meetingEntityList = meetingRepository.findByMeetingStatusAndConfirmedDate(LocalDate.now(), true, MeetingStatus.CONFIRMED);
        for (MeetingEntity meeting : meetingEntityList) {
            meeting.setMeetingStatus(MeetingStatus.DONE);
        }

        meetingRepository.saveAll(meetingEntityList);
    }
}
