package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.*;
import com.promise8.wwwbe.model.entity.*;
import com.promise8.wwwbe.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

        List<MeetingPlaceEntity> meetingPlaceEntityList = new ArrayList<>();
        for (String promisePlace : meetingCreateReqDto.getPromisePlaceList()) {
            meetingPlaceEntityList.add(MeetingPlaceEntity.builder()
                    .promisePlace(promisePlace)
                    .meetingUserEntity(meetingUserEntity)
                    .build());
        }
        meetingPlaceRepository.saveAll(meetingPlaceEntityList);

        // TODO Fix deviceType
        return MeetingCreateResDto.of(meetingCode, getDynamicLink(true));
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

    private String getMeetingCode() {
        String code = RandomStringUtils.random(MEETING_CODE_LENGTH, true, false);
        while (true) {
            String existMeetingCode = meetingRepository.findByMeetingCode(code);
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

    private String getDynamicLink(boolean deviceType) {
        String dynamicLinkUrl = "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        DynamicLinkReqDto dynamicLinkReqDto = null;
        if (deviceType) {
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
        return getMeeting(meetingEntity);
    }

    public MeetingGetRes getMeetingByCode(String meetingCode) {
        MeetingEntity meetingEntity = meetingRepository.findByMeetingCode(meetingCode).orElseThrow();
        return getMeeting(meetingEntity);
    }

    public MeetingGetRes getMeeting(MeetingEntity meetingEntity) {
        List<MeetingUserEntity> meetingUserEntityList = meetingUserRepository.findByMeetingEntity_MeetingId(meetingEntity.getMeetingId());
        List<MeetingUserTimetableEntity> meetingUserTimetableEntityList = meetingUserTimetableRepository.findByMeetingUserEntity_MeetingEntity_MeetingId(meetingEntity.getMeetingId());
        List<MeetingPlaceEntity> meetingPlaceEntityList = meetingPlaceRepository.findByMeetingUserEntity_MeetingEntity_MeetingId(meetingEntity.getMeetingId());
        List<PlaceVoteEntity> placeVoteEntityList = placeVoteRepository.findByMeetingPlaceEntity_MeetingUserEntity_MeetingEntity_MeetingId(meetingEntity.getMeetingId());
        MeetingUserEntity meetingUserEntity = meetingUserRepository.findByUserEntity_UserIdAndMeetingEntity_MeetingId(meetingEntity.getUserEntity().getUserId(), meetingEntity.getMeetingId()).orElseThrow();

        return MeetingGetRes.of(
                meetingEntity,
                meetingUserEntityList,
                meetingUserTimetableEntityList,
                meetingPlaceEntityList,
                placeVoteEntityList,
                meetingUserEntity.getMeetingUserName()
        );
    }
}
