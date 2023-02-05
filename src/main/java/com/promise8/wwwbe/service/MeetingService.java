package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.*;
import com.promise8.wwwbe.model.entity.*;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import com.promise8.wwwbe.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private static final int MEETING_CODE_LENGTH = 6;
    private static final String ANDROID_PACKAGE = "com.promiseeight.www";
    private static final String IOS_PACKAGE = "com.promise8.www";
    // TODO Fix link
    private static final String LONG_DYNAMIC_LINK = "https://whenwheres.page.link/?link=https://naver.com";
    private final PushService pushService;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final MeetingUserTimetableRepository meetingUserTimetableRepository;
    private final MeetingPlaceRepository meetingPlaceRepository;
    private final PlaceVoteRepository placeVoteRepository;
    @Value("${api-key.android}")
    private String androidApiKey;
    @Value("${api-key.ios}")
    private String iosApiKey;

    public MeetingCreateResDto createMeeting(MeetingCreateReqDto meetingCreateReqDto) {
        String meetingCode = getMeetingCode();
        UserEntity userEntity = getUser(meetingCreateReqDto.getDeviceId(), meetingCreateReqDto.getUserName());
        MeetingEntity meetingEntity = meetingRepository.save(meetingCreateReqDto.of(userEntity, meetingCode));
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

        return MeetingCreateResDto.of(meetingCode, getDynamicLink(meetingCreateReqDto.getPlatformType()));
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
        return optionalUserEntity.orElseGet(() -> userRepository.save(UserEntity.builder()
                .deviceId(deviceId)
                .userName(userName)
                .build()
        ));
    }

    private String getDynamicLink(PlatformType platformType) {
        String dynamicLinkUrl = "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        DynamicLinkReqDto dynamicLinkReqDto = null;
        if (PlatformType.ANDROID.equals(platformType)) {
            dynamicLinkReqDto = DynamicLinkReqDto.of(LONG_DYNAMIC_LINK + "&apn=" + ANDROID_PACKAGE);
            dynamicLinkUrl += androidApiKey;
        } else {
            dynamicLinkReqDto = DynamicLinkReqDto.of(LONG_DYNAMIC_LINK + "&ibi=" + IOS_PACKAGE);
            dynamicLinkUrl += iosApiKey;
        }

        HttpEntity<DynamicLinkReqDto> dynamicLinkReq = new HttpEntity<>(dynamicLinkReqDto, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HashMap> response = restTemplate.exchange(dynamicLinkUrl, HttpMethod.POST, dynamicLinkReq, HashMap.class);

        return response.getBody().get("shortLink").toString();
    }

    public MeetingGetResDto getMeetingById(long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow();

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
                confirmedPromiseDto
        );
    }

    public MeetingGetResDto getMeetingByCode(String meetingCode) {
        MeetingEntity meetingEntity = meetingRepository.findByMeetingCode(meetingCode).orElseThrow();

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
                confirmedPromiseDto
        );
    }

    public MeetingMainGetResDtoWrapper getMeetingByDeviceId(String deviceId) {
        List<MeetingEntity> meetingEntityList = meetingRepository.findByUserEntity_DeviceId(deviceId);

        return MeetingMainGetResDtoWrapper.of(meetingEntityList);
    }

    private List<UserPromiseTimeResDto> getUserPromiseTimeList(MeetingEntity meetingEntity) {
        List<UserPromiseTimeResDto> userPromiseTimeResDtoList = new ArrayList<>();

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
        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            userPromisePlaceResDtoList.addAll(meetingUser.getMeetingPlaceEntityList().stream()
                    .map(UserPromisePlaceResDto::of).collect(Collectors.toList()));
        });

        return userPromisePlaceResDtoList;
    }
}
