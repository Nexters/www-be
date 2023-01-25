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

import java.time.LocalDateTime;
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
    @Value("${api-key.android}")
    private String androidApiKey;
    @Value("${api-key.ios}")
    private String iosApiKey;

    public MeetingCreateResDto createMeeting(MeetingCreateReqDto meetingCreateReqDto) {
        String meetingCode = RandomStringUtils.random(MEETING_CODE_LENGTH, true, false);
        UserEntity userEntity = getUser(meetingCreateReqDto.getDeviceId(), meetingCreateReqDto.getUserName());
        MeetingEntity meetingEntity = meetingRepository.save(new MeetingEntity(meetingCreateReqDto, userEntity, meetingCode));
        MeetingUserEntity meetingUserEntity = meetingUserRepository.save(new MeetingUserEntity(meetingCreateReqDto.getUserName(), userEntity, meetingEntity));

        for (PromiseDateAndTimeDto promiseDateAndTimeDto : meetingCreateReqDto.getPromiseDateAndTimeDtoList()) {
            LocalDateTime promiseDate = promiseDateAndTimeDto.getPromiseDate();
            List<PromiseTime> promiseTimeList = promiseDateAndTimeDto.getPromiseTimeList();
            if (promiseTimeList == null || promiseTimeList.size() == 0) {
                // TODO Check promiseTime is possible null
                meetingUserTimetableRepository.save(new MeetingUserTimetableEntity(promiseDate, null, meetingUserEntity));
            } else {
                for (PromiseTime promiseTime : promiseTimeList) {
                    meetingUserTimetableRepository.save(new MeetingUserTimetableEntity(promiseDate, promiseTime.name(), meetingUserEntity));
                }
            }
        }

        for (String promisePlace : meetingCreateReqDto.getPromisePlaceList()) {
            meetingPlaceRepository.save(new MeetingPlaceEntity(promisePlace, meetingUserEntity));
        }

        // TODO Fix deviceType
        return new MeetingCreateResDto(meetingCode, getDynamicLink(true));
    }

    private UserEntity getUser(String deviceId, String userName) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByDeviceId(deviceId);
        return optionalUserEntity.orElseGet(() -> userRepository.save(new UserEntity(deviceId, userName)));
    }

    private String getDynamicLink(boolean deviceType) {
        String dynamicLinkUrl = "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        DynamicLinkReqDto dynamicLinkReqDto = null;
        if (deviceType) {
            dynamicLinkReqDto = new DynamicLinkReqDto(LONG_DYNAMIC_LINK + "&apn=" + ANDROID_PACKAGE);
            dynamicLinkUrl += androidApiKey;
        } else {
            dynamicLinkReqDto = new DynamicLinkReqDto(LONG_DYNAMIC_LINK + "&ibi=" + IOS_PACKAGE);
            dynamicLinkUrl += iosApiKey;
        }

        HttpEntity<DynamicLinkReqDto> dynamicLinkReq = new HttpEntity<>(dynamicLinkReqDto, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HashMap> response = restTemplate.exchange(dynamicLinkUrl, HttpMethod.POST, dynamicLinkReq, HashMap.class);

        return response.getBody().get("shortLink").toString();
    }
}
