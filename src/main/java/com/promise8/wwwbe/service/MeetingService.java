package com.promise8.wwwbe.service;

import com.promise8.wwwbe.repository.*;
import com.promise8.wwwbe.v1.model.dto.PromiseTime;
import com.promise8.wwwbe.v1.model.dto.req.JoinMeetingReqDtoV1;
import com.promise8.wwwbe.v1.model.dto.req.MeetingConfirmDtoV1;
import com.promise8.wwwbe.v1.model.dto.req.MeetingCreateReqDtoV1;
import com.promise8.wwwbe.v1.model.dto.req.UserPromiseTimeReqDtoV1;
import com.promise8.wwwbe.v1.model.dto.res.*;
import com.promise8.wwwbe.v1.model.entity.*;
import com.promise8.wwwbe.v1.model.exception.BizException;
import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private static final int MEETING_CODE_LENGTH = 6;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final MeetingUserTimetableRepository meetingUserTimetableRepository;
    private final MeetingPlaceRepository meetingPlaceRepository;
    private final LinkService linkService;
    private final PushService pushService;

    @Transactional
    public MeetingCreateResDtoV1 createMeeting(MeetingCreateReqDtoV1 meetingCreateReqDto, String deviceId) {
        String meetingCode = getMeetingCode();
        UserEntityV1 userEntity = getUser(deviceId, meetingCreateReqDto.getUserName());
        DynamicLinkResDtoV1 dynamicLinkResDto = linkService.createLink(meetingCode);
        String shortLink = null;
        if (dynamicLinkResDto != null) {
            shortLink = dynamicLinkResDto.getShortLink();
        }

        MeetingEntityV1 meetingEntity = meetingRepository.save(meetingCreateReqDto.of(userEntity, meetingCode, shortLink));
        MeetingUserEntityV1 meetingUserEntity = meetingUserRepository.save(MeetingUserEntityV1.builder()
                .meetingUserName(meetingCreateReqDto.getUserName())
                .userEntity(userEntity)
                .meetingEntity(meetingEntity)
                .build()
        );

        // TODO Refactoring
        List<MeetingUserTimetableEntityV1> meetingUserTimetableEntityList = new ArrayList<>();

        for (UserPromiseTimeReqDtoV1 userPromiseTimeReqDto : meetingCreateReqDto.getPromiseDateTimeList()) {
            LocalDate promiseDate = userPromiseTimeReqDto.getPromiseDate();
            PromiseTime promiseTime = userPromiseTimeReqDto.getPromiseTime();

            MeetingUserTimetableEntityV1 meetingUserTimetableEntity = MeetingUserTimetableEntityV1.builder()
                    .promiseDate(promiseDate)
                    .promiseTime(promiseTime)
                    .isConfirmed(false)
                    .meetingUserEntity(meetingUserEntity)
                    .build();
            meetingUserTimetableEntityList.add(meetingUserTimetableEntity);
        }
        meetingUserTimetableRepository.saveAll(meetingUserTimetableEntityList);

        // TODO Refactoring
        HashSet<String> isExistPlaceHashSet = new HashSet<>();
        List<MeetingPlaceEntityV1> meetingPlaceEntityList = new ArrayList<>();
        for (String promisePlace : meetingCreateReqDto.getPromisePlaceList()) {
            if (isExistPlaceHashSet.contains(promisePlace)) {
                continue;
            } else {
                isExistPlaceHashSet.add(promisePlace);
            }

            meetingPlaceEntityList.add(MeetingPlaceEntityV1.builder()
                    .promisePlace(promisePlace)
                    .isConfirmed(false)
                    .meetingUserEntity(meetingUserEntity)
                    .build());
        }
        meetingPlaceRepository.saveAll(meetingPlaceEntityList);

        return MeetingCreateResDtoV1.of(meetingCode, shortLink);
    }

    @Transactional
    public void putMeetingStatus(long meetingId, MeetingStatusV1 meetingStatus) {
        MeetingEntityV1 meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.NOT_EXIST_MEETING);
        });

        if (MeetingStatusV1.WAITING.equals(meetingStatus) || MeetingStatusV1.DONE.equals(meetingStatus)) {
            throw new BizException(BaseErrorCode.INVALID_REQUEST);
        }

        meetingEntity.setMeetingStatus(meetingStatus);
        meetingRepository.save(meetingEntity);

//        List<UserEntityV1> userEntityList = meetingEntity.getMeetingUserEntityList().stream()
//                .map(MeetingUserEntityV1::getUserEntity)
//                .collect(Collectors.toList());
//
//        if (MeetingStatusV1.VOTING.equals(meetingStatus)) {
//            for (UserEntityV1 user : userEntityList) {
//                if (!user.getIsAlarmOn()) {
//                    continue;
//                }
//                pushService.send(
//                        user.getFcmToken(),
//                        new PushMessageV1(
//                                PushMessageV1.ContentType.MEETING,
//                                meetingId,
//                                meetingEntity.getMeetingName(),
//                                "장소 선정 투표가 시작되었어요.\n내가 선호하는 장소에 투표해보세요!"
//                        )
//                );
//            }
//        }

        if (MeetingStatusV1.VOTED.equals(meetingStatus)) {
//            for (UserEntityV1 user : userEntityList) {
//                if (!user.getIsAlarmOn()) {
//                    continue;
//                }
//                pushService.send(
//                        user.getFcmToken(),
//                        new PushMessageV1(
//                                PushMessageV1.ContentType.MEETING,
//                                meetingId,
//                                meetingEntity.getMeetingName(),
//                                "장소 선정 투표가 완료되었어요.\n투표 결과를 확인해보세요!"
//                        )
//                );
//            }
            MeetingEntityV1 votedMeeting = meetingRepository.findById(meetingId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
            votedMeeting.setVoteFinishDateTime(LocalDateTime.now());
        }
    }

    @Transactional
    public void confirmMeeting(long meetingId, MeetingConfirmDtoV1 meetingConfirmDto) {
        long meetingPlaceId = meetingConfirmDto.getMeetingPlaceId();

        MeetingPlaceEntityV1 meetingPlaceEntity = meetingPlaceRepository.findById(meetingPlaceId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING_PLACE));
        MeetingUserTimetableEntityV1 meetingUserTimetableEntity = meetingUserTimetableRepository.findById(meetingConfirmDto.getMeetingUserTimetableId()).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING_TIMETABLE));
        meetingPlaceEntity.setIsConfirmed(true);
        meetingUserTimetableEntity.setIsConfirmed(true);

        meetingPlaceRepository.save(meetingPlaceEntity);
        meetingUserTimetableRepository.save(meetingUserTimetableEntity);

        MeetingEntityV1 meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        meetingEntity.setMeetingStatus(MeetingStatusV1.CONFIRMED);
        meetingRepository.save(meetingEntity);
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

    private UserEntityV1 getUser(String deviceId, String userName) {
        Optional<UserEntityV1> optionalUserEntity = userRepository.findByDeviceId(deviceId);

        UserEntityV1 userEntity = null;
        if (optionalUserEntity.isPresent()) {
            userEntity = optionalUserEntity.get();
            userEntity.setUserName(userName);
            return userRepository.save(userEntity);
        } else {
            return userRepository.save(UserEntityV1.builder()
                    .deviceId(deviceId)
                    .userName(userName)
                    .build());
        }
    }

    public MeetingGetResDtoV1 getMeetingById(long meetingId, long currentUserId) {
        MeetingEntityV1 meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        return getMeetingInfo(meetingEntity, currentUserId);
    }

    public MeetingGetResDtoV1 getMeetingByCode(String meetingCode, long currentUserId) {
        MeetingEntityV1 meetingEntity = meetingRepository.findByMeetingCode(meetingCode)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        return getMeetingInfo(meetingEntity, currentUserId);
    }

    private MeetingGetResDtoV1 getMeetingInfo(MeetingEntityV1 meetingEntity, long currentUserId) {
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            throw new BizException(BaseErrorCode.SERVER_ERROR);
        }

        boolean isJoined = meetingEntity.getMeetingUserEntityList().stream()
                .anyMatch(meetingUserEntity -> meetingUserEntity.getUserEntity().getUserId() == currentUserId);

        if (!isJoined && !MeetingStatusV1.WAITING.equals(meetingEntity.getMeetingStatus())) {
            throw new BizException(BaseErrorCode.ALREADY_VOTING_MEETING);
        }

        ConfirmedPromiseResDtoV1 confirmedPromiseResDto = null;
        if (MeetingStatusV1.DONE.equals(meetingEntity.getMeetingStatus()) || MeetingStatusV1.CONFIRMED.equals(meetingEntity.getMeetingStatus())) {
            confirmedPromiseResDto = MeetingServiceHelper.getConfirmedPromise(meetingEntity.getMeetingUserEntityList(), meetingEntity.getCreator().getUserId());
        } else {
            confirmedPromiseResDto = MeetingServiceHelper.getHostAndVotingCnt(meetingEntity.getMeetingUserEntityList(), meetingEntity.getCreator().getUserId());
        }

        UserEntityV1 userEntity = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));

        return MeetingGetResDtoV1.of(
                meetingEntity,
                MeetingServiceHelper.getMeetingUserInfoDtoList(meetingEntity),
                MeetingServiceHelper.getUserPromisePlaceResDtoList(meetingEntity),
                MeetingServiceHelper.getUserPromiseTimeList(meetingEntity),
                MeetingServiceHelper.getUserVoteHashMap(meetingEntity),
                confirmedPromiseResDto,
                userEntity,
                isJoined
        );
    }

    public MeetingMainGetResDtoWrapperV1 getMeetingListByDeviceId(String deviceId) {
        List<MeetingEntityV1> meetingEntityList = meetingRepository.findByUserEntity_DeviceId(deviceId);

        return MeetingMainGetResDtoWrapperV1.of(meetingEntityList);
    }

    public Long joinMeetingAndGetMeetingUserId(long userId, long meetingId, JoinMeetingReqDtoV1 joinMeetingReqDto) {
        Boolean isExistMeetingUser = meetingUserRepository.existsMeetingUserEntityByUserEntity_UserIdAndMeetingEntity_MeetingId(userId, meetingId);
        if (isExistMeetingUser) {
            throw new BizException(BaseErrorCode.ALREADY_PARTICIPATED_MEETING);
        }
        MeetingEntityV1 meetingEntity =
                meetingRepository.findById(meetingId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        UserEntityV1 userEntity =
                userRepository.findById(userId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));
        userEntity.setUserName(joinMeetingReqDto.getNickname());
        userRepository.save(userEntity);

        MeetingUserEntityV1 newMeetingUserEntity = MeetingUserEntityV1.builder()
                .userEntity(userEntity)
                .meetingEntity(meetingEntity)
                .meetingUserName(joinMeetingReqDto.getNickname())
                .build();

        MeetingUserEntityV1 meetingUserEntity = meetingEntity.addMeetingUser(newMeetingUserEntity);
        meetingUserRepository.save(meetingUserEntity);

        List<MeetingUserTimetableEntityV1> meetingUserTimetableEntityList = new ArrayList<>();
        for (UserPromiseTimeReqDtoV1 userPromiseTimeReq : joinMeetingReqDto.getUserPromiseTimeList()) {
            meetingUserTimetableEntityList.add(MeetingUserTimetableEntityV1.builder()
                    .meetingUserEntity(meetingUserEntity)
                    .promiseDate(userPromiseTimeReq.getPromiseDate())
                    .promiseTime(userPromiseTimeReq.getPromiseTime())
                    .isConfirmed(false)
                    .build());
        }

        meetingUserTimetableRepository.saveAll(meetingUserTimetableEntityList);

        HashSet<String> existPromisePlaceHashSet = new HashSet<>();
        if (meetingEntity.getMeetingUserEntityList() != null) {
            meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
                existPromisePlaceHashSet.addAll(meetingPlaceRepository.getExistMeetingPlaceList(meetingUser));
            });
        }

        List<MeetingPlaceEntityV1> meetingPlaceEntityList = new ArrayList<>();
        joinMeetingReqDto.getPromisePlaceList().forEach(req -> {
            if (existPromisePlaceHashSet.contains(req)) {
                return;
            } else {
                existPromisePlaceHashSet.add(req);
            }

            meetingPlaceEntityList.add(meetingPlaceRepository.save(MeetingPlaceEntityV1.builder()
                    .meetingUserEntity(meetingUserEntity)
                    .promisePlace(req)
                    .isConfirmed(false)
                    .build()));
        });

//        int currentUserCount = meetingEntity.getMeetingUserEntityList().size();
//
//        if (currentUserCount == meetingEntity.getConditionCount()) {
//            if (meetingEntity.getCreator().getIsAlarmOn()) {
//                pushService.send(
//                        meetingEntity.getCreator().getFcmToken(),
//                        new PushMessageV1(
//                                PushMessageV1.ContentType.MEETING,
//                                meetingId,
//                                meetingEntity.getMeetingName(),
//                                "약속 예상 인원이 다 모였어요.\n약속방에서 투표를 시작해보세요!"
//                        )
//                );
//            }
//        }

        meetingPlaceRepository.saveAll(meetingPlaceEntityList);
        return meetingUserEntity.getMeetingUserId();
    }

    public List<MeetingEntityV1> getVoteNotiNeedMeetingList() {
        return meetingRepository.findVotedMeetingByDateTime(LocalDate.now().minusDays(1).atStartOfDay());
    }

    @Scheduled(cron = "2 0 0 * * ?", zone = "Asia/Seoul")
    public void promiseDone() {
        List<MeetingEntityV1> meetingEntityList = meetingRepository.findByMeetingStatusAndConfirmedDate(LocalDate.now(), true, MeetingStatusV1.CONFIRMED);
        for (MeetingEntityV1 meeting : meetingEntityList) {
            meeting.setMeetingStatus(MeetingStatusV1.DONE);
        }

        meetingRepository.saveAll(meetingEntityList);
    }

//    @Scheduled(cron = "0 0 18 * * ?", zone = "Asia/Seoul")
//    public void getDDay() {
//        List<MeetingEntityV1> meetingEntityList =
//                meetingRepository.getMeetingOneDayLater(LocalDate.now().plusDays(1L), true, MeetingStatusV1.CONFIRMED);
//
//        for (MeetingEntityV1 meetingEntity : meetingEntityList) {
//            for (MeetingUserEntityV1 meetingUserEntity : meetingEntity.getMeetingUserEntityList()) {
//                if (!meetingUserEntity.getUserEntity().getIsAlarmOn()) {
//                    continue;
//                }
//
//                String confirmedTime = meetingUserTimetableRepository.findConfirmedPromiseTime(meetingUserEntity.getMeetingUserId());
//                String confirmedPlace = meetingPlaceRepository.findConfirmedPromiseTime(meetingUserEntity.getMeetingUserId());
//                pushService.send(
//                        meetingUserEntity.getUserEntity().getFcmToken(),
//                        new PushMessageV1(
//                                PushMessageV1.ContentType.MEETING,
//                                meetingEntity.getMeetingId(),
//                                meetingEntity.getMeetingName(),
//                                "내일은 " + confirmedTime + "에 " + confirmedPlace + "에서 약속이 있어요!")
//                );
//            }
//        }
//    }
//
//    @Scheduled(cron = "0 0 18 * * ?", zone = "Asia/Seoul")
//    public void confirmRequestNoti() {
//        List<MeetingEntityV1> meetingEntityList = getVoteNotiNeedMeetingList();
//
//        for (MeetingEntityV1 meetingEntity : meetingEntityList) {
//            UserEntityV1 creator = meetingEntity.getCreator();
//            if (creator.getIsAlarmOn()) {
//                pushService.send(
//                        creator.getFcmToken(),
//                        new PushMessageV1(
//                                PushMessageV1.ContentType.MEETING,
//                                meetingEntity.getMeetingId(),
//                                meetingEntity.getMeetingName(),
//                                "약속장소와 시간이 확정되었나요?\n약속정보를 확정해주세요!"));
//            }
//        }
//    }
}
