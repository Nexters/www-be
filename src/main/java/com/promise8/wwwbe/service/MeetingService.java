package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.MeetingCreateReqDto;
import com.promise8.wwwbe.model.dto.MeetingCreateResDto;
import com.promise8.wwwbe.model.dto.PromiseDateAndTimeDto;
import com.promise8.wwwbe.model.entity.*;
import com.promise8.wwwbe.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final int MEETING_CODE_LENGTH = 6;
    private final PushService pushService;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final MeetingUserTimetableRepository meetingUserTimetableRepository;
    private final MeetingPlaceRepository meetingPlaceRepository;

    public MeetingCreateResDto createMeeting(MeetingCreateReqDto meetingCreateReqDto) {
        String meetingCode = RandomStringUtils.random(MEETING_CODE_LENGTH, true, false);
        UserEntity userEntity = getUser(meetingCreateReqDto.getDeviceId(), meetingCreateReqDto.getUserName());
        MeetingEntity meetingEntity = meetingRepository.save(new MeetingEntity(meetingCreateReqDto, userEntity, meetingCode));
        MeetingUserEntity meetingUserEntity = meetingUserRepository.save(new MeetingUserEntity(meetingCreateReqDto.getUserName(), userEntity, meetingEntity));

        for (PromiseDateAndTimeDto promiseDateAndTimeDto : meetingCreateReqDto.getPromiseDateAndTimeDtoList()) {
            LocalDateTime promiseDate = promiseDateAndTimeDto.getPromiseDate();
            List<String> promiseTimeList = promiseDateAndTimeDto.getPromiseTimeList();
            if (promiseTimeList == null || promiseTimeList.size() == 0) {
                // TODO Check promiseTime is possible null
                meetingUserTimetableRepository.save(new MeetingUserTimetableEntity(promiseDate, null, meetingUserEntity));
            } else {
                for (String promiseTime : promiseTimeList) {
                    meetingUserTimetableRepository.save(new MeetingUserTimetableEntity(promiseDate, promiseTime, meetingUserEntity));
                }
            }
        }

        for (String promisePlace : meetingCreateReqDto.getPromisePlaceList()) {
            meetingPlaceRepository.save(new MeetingPlaceEntity(promisePlace, meetingUserEntity));
        }

        return new MeetingCreateResDto(meetingCode, null);
    }

    private UserEntity getUser(String deviceId, String userName) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByDeviceId(deviceId);
        return optionalUserEntity.orElseGet(() -> userRepository.save(new UserEntity(deviceId, userName)));
    }

}
