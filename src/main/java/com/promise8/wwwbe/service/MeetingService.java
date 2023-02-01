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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
                            .promiseTime(promiseTime.name())
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
    public void putMeetingStatus(long meetingId, ActionType actionType) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.INVALID_REQUEST, "not exist meeting");
        });

        if (ActionType.END_VOTE.equals(actionType)) {
            meetingEntity.setMeetingStatus(MeetingStatus.VOTED);
        }

        if (ActionType.END_MEETING.equals(actionType)) {
            meetingEntity.setMeetingStatus(MeetingStatus.CONFIRMED);
        }
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

    public MeetingGetRes getMeetingById(long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow();

        return MeetingGetRes.of(
                meetingEntity,
                getUserPromisePlaceResDtoList(meetingEntity),
                getUserPromiseTimeHashMap(meetingEntity),
                getUserVoteHashMap(meetingEntity)
        );
    }

    public MeetingGetRes getMeetingByCode(String meetingCode) {
        MeetingEntity meetingEntity = meetingRepository.findByMeetingCode(meetingCode).orElseThrow();

        return MeetingGetRes.of(
                meetingEntity,
                getUserPromisePlaceResDtoList(meetingEntity),
                getUserPromiseTimeHashMap(meetingEntity),
                getUserVoteHashMap(meetingEntity)
        );
    }

    public List<MeetingGetRes> getMeetingByDeviceId(String deviceId) {
        List<MeetingEntity> meetingEntityList = meetingRepository.findByUserEntity_DeviceId(deviceId);

        // TODO Add more
        return null;
    }

    private HashMap<LocalDate, List<String[]>> getUserPromiseTimeHashMap(MeetingEntity meetingEntity) {
        HashMap<LocalDate, List<String[]>> userPromiseTimeHashMap = new HashMap<>();

        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            meetingUser.getMeetingUserTimetableEntityList().forEach(res -> {
                LocalDate promiseDate = res.getPromiseDate();
                if (userPromiseTimeHashMap.containsKey(promiseDate)) {
                    userPromiseTimeHashMap.get(promiseDate).add(new String[]{res.getMeetingUserEntity().getMeetingUserName(), res.getPromiseTime()});
                } else {
                    List<String[]> promiseList = new ArrayList<>();
                    promiseList.add(new String[]{res.getMeetingUserEntity().getMeetingUserName(), res.getPromiseTime()});
                    userPromiseTimeHashMap.put(promiseDate, promiseList);
                }
            });
        });

        return userPromiseTimeHashMap;
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
                        List<String> voteList = new ArrayList<>();
                        voteList.add(res.getMeetingUserEntity().getMeetingUserName());
                        userVoteHashMap.put(promisePlace, voteList);
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
