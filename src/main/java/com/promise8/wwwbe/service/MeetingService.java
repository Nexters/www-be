package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.PromiseTime;
import com.promise8.wwwbe.model.dto.req.JoinMeetingReqDto;
import com.promise8.wwwbe.model.dto.req.MeetingConfirmDto;
import com.promise8.wwwbe.model.dto.req.MeetingCreateReqDto;
import com.promise8.wwwbe.model.dto.req.UserPromiseTimeReqDto;
import com.promise8.wwwbe.model.dto.res.*;
import com.promise8.wwwbe.model.entity.*;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import com.promise8.wwwbe.model.mobile.PushMessage;
import com.promise8.wwwbe.repository.*;
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
import java.util.stream.Collectors;

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
    public MeetingCreateResDto createMeeting(MeetingCreateReqDto meetingCreateReqDto, String deviceId) {
        String meetingCode = getMeetingCode();
        UserEntity userEntity = getUser(deviceId, meetingCreateReqDto.getUserName());
        DynamicLinkResDto dynamicLinkResDto = linkService.createLink(meetingCode);
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

        for (UserPromiseTimeReqDto userPromiseTimeReqDto : meetingCreateReqDto.getPromiseDateTimeList()) {
            LocalDate promiseDate = userPromiseTimeReqDto.getPromiseDate();
            PromiseTime promiseTime = userPromiseTimeReqDto.getPromiseTime();

            MeetingUserTimetableEntity meetingUserTimetableEntity = MeetingUserTimetableEntity.builder()
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
        List<MeetingPlaceEntity> meetingPlaceEntityList = new ArrayList<>();
        for (String promisePlace : meetingCreateReqDto.getPromisePlaceList()) {
            if (isExistPlaceHashSet.contains(promisePlace)) {
                continue;
            } else {
                isExistPlaceHashSet.add(promisePlace);
            }

            meetingPlaceEntityList.add(MeetingPlaceEntity.builder()
                    .promisePlace(promisePlace)
                    .isConfirmed(false)
                    .meetingUserEntity(meetingUserEntity)
                    .build());
        }
        meetingPlaceRepository.saveAll(meetingPlaceEntityList);

        return MeetingCreateResDto.of(meetingCode, shortLink);
    }

    @Transactional
    public void putMeetingStatus(long meetingId, MeetingStatus meetingStatus) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.NOT_EXIST_MEETING);
        });

        if (MeetingStatus.WAITING.equals(meetingStatus) || MeetingStatus.DONE.equals(meetingStatus)) {
            throw new BizException(BaseErrorCode.INVALID_REQUEST);
        }

        meetingEntity.setMeetingStatus(meetingStatus);
        meetingRepository.save(meetingEntity);

        List<UserEntity> userEntityList = meetingEntity.getMeetingUserEntityList().stream()
                .map(MeetingUserEntity::getUserEntity)
                .collect(Collectors.toList());

        if (MeetingStatus.VOTING.equals(meetingStatus)) {
            for (UserEntity user : userEntityList) {
                if (!user.getIsAlarmOn()) {
                    continue;
                }
                pushService.send(user.getFcmToken(), new PushMessage(PushMessage.ContentType.MEETING, meetingId, meetingEntity.getMeetingName(), "장소 선정 투표가 시작되었어요.\n내가 선호하는 장소에 투표해보세요!"));
            }
        }

        if (MeetingStatus.VOTED.equals(meetingStatus)) {
            for (UserEntity user : userEntityList) {
                if (!user.getIsAlarmOn()) {
                    continue;
                }
                pushService.send(user.getFcmToken(), new PushMessage(PushMessage.ContentType.MEETING, meetingId, meetingEntity.getMeetingName(), "장소 선정 투표가 완료되었어요.\n투표 결과를 확인해보세요!"));
            }
            MeetingEntity votedMeeting = meetingRepository.findById(meetingId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
            votedMeeting.setVoteFinishDateTime(LocalDateTime.now());
        }
    }

    @Transactional
    public void confirmMeeting(MeetingConfirmDto meetingConfirmDto) {
        long meetingPlaceId = meetingConfirmDto.getMeetingPlaceId();

        MeetingPlaceEntity meetingPlaceEntity = meetingPlaceRepository.findById(meetingPlaceId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING_PLACE));
        MeetingUserTimetableEntity meetingUserTimetableEntity = meetingUserTimetableRepository.findById(meetingConfirmDto.getMeetingUserTimetableId()).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING_TIMETABLE));
        meetingPlaceEntity.setIsConfirmed(true);
        meetingUserTimetableEntity.setIsConfirmed(true);

        meetingPlaceRepository.save(meetingPlaceEntity);
        meetingUserTimetableRepository.save(meetingUserTimetableEntity);
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
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        return getMeetingInfo(meetingEntity, currentUserId);
    }

    public MeetingGetResDto getMeetingByCode(String meetingCode, long currentUserId) {
        MeetingEntity meetingEntity = meetingRepository.findByMeetingCode(meetingCode)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        return getMeetingInfo(meetingEntity, currentUserId);
    }

    private MeetingGetResDto getMeetingInfo(MeetingEntity meetingEntity, long currentUserId) {
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            throw new BizException(BaseErrorCode.SERVER_ERROR);
        }

        boolean isJoined = meetingEntity.getMeetingUserEntityList().stream()
                .anyMatch(meetingUserEntity -> meetingUserEntity.getUserEntity().getUserId() == currentUserId);

        if (!isJoined && !MeetingStatus.WAITING.equals(meetingEntity.getMeetingStatus())) {
            throw new BizException(BaseErrorCode.ALREADY_VOTING_MEETING);
        }

        ConfirmedPromiseResDto confirmedPromiseResDto = null;
        if (MeetingStatus.DONE.equals(meetingEntity.getMeetingStatus()) || MeetingStatus.CONFIRMED.equals(meetingEntity.getMeetingStatus())) {
            confirmedPromiseResDto = MeetingServiceHelper.getConfirmedPromise(meetingEntity.getMeetingUserEntityList(), meetingEntity.getCreator().getUserId());
        } else {
            confirmedPromiseResDto = MeetingServiceHelper.getHostAndVotingCnt(meetingEntity.getMeetingUserEntityList(), meetingEntity.getCreator().getUserId());
        }

        UserEntity userEntity = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));

        return MeetingGetResDto.of(
                meetingEntity,
                MeetingServiceHelper.getMeetingUserInfoDtoList(meetingEntity),
                getUserPromisePlaceResDtoList(meetingEntity),
                MeetingServiceHelper.getUserPromiseTimeList(meetingEntity),
                MeetingServiceHelper.getUserVoteHashMap(meetingEntity),
                confirmedPromiseResDto,
                userEntity,
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
                meetingRepository.findById(meetingId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        UserEntity userEntity =
                userRepository.findById(userId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));
        userEntity.setUserName(joinMeetingReqDto.getNickname());
        userRepository.save(userEntity);

        MeetingUserEntity newMeetingUserEntity = MeetingUserEntity.builder()
                .userEntity(userEntity)
                .meetingEntity(meetingEntity)
                .meetingUserName(joinMeetingReqDto.getNickname())
                .build();

        MeetingUserEntity meetingUserEntity = meetingEntity.addMeetingUser(newMeetingUserEntity);

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

        HashSet<String> existPromisePlaceHashSet = new HashSet<>();
        if (meetingEntity.getMeetingUserEntityList() != null) {
            meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
                existPromisePlaceHashSet.addAll(meetingPlaceRepository.getExistMeetingPlaceList(meetingUser));
            });
        }

        List<MeetingPlaceEntity> meetingPlaceEntityList = new ArrayList<>();
        joinMeetingReqDto.getPromisePlaceList().forEach(req -> {
            if (existPromisePlaceHashSet.contains(req)) {
                return;
            } else {
                existPromisePlaceHashSet.add(req);
            }

            meetingPlaceEntityList.add(meetingPlaceRepository.save(MeetingPlaceEntity.builder()
                    .meetingUserEntity(meetingUserEntity)
                    .promisePlace(req)
                    .isConfirmed(false)
                    .build()));
        });

        int currentUserCount = meetingEntity.getMeetingUserEntityList().size();

        if (currentUserCount == meetingEntity.getConditionCount()) {
            if (meetingEntity.getCreator().getIsAlarmOn()) {
                pushService.send(
                        meetingEntity.getCreator().getFcmToken(),
                        new PushMessage(PushMessage.ContentType.MEETING, meetingId, meetingEntity.getMeetingName(), "약속 예상 인원이 다 모였어요.\n약속방에서 투표를 시작해보세요!"));
            }
        }

        meetingPlaceRepository.saveAll(meetingPlaceEntityList);
        return meetingUserEntity.getMeetingUserId();
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

    public List<MeetingEntity> getVoteNotiNeedMeetingList() {
        return meetingRepository.findVotedMeetingByDateTime(LocalDate.now().minusDays(1).atStartOfDay());
    }

    @Scheduled(cron = "2 0 0 * * ?", zone = "Asia/Seoul")
    public void promiseDone() {
        List<MeetingEntity> meetingEntityList = meetingRepository.findByMeetingStatusAndConfirmedDate(LocalDate.now(), true, MeetingStatus.CONFIRMED);
        for (MeetingEntity meeting : meetingEntityList) {
            meeting.setMeetingStatus(MeetingStatus.DONE);
        }

        meetingRepository.saveAll(meetingEntityList);
    }

    @Scheduled(cron = "0 0 18 * * ?", zone = "Asia/Seoul")
    public void getDDay() {
        List<MeetingEntity> meetingEntityList = meetingRepository.getMeetingOneDayLater(LocalDate.now().plusDays(1L), true, MeetingStatus.CONFIRMED);
        for (MeetingEntity meetingEntity : meetingEntityList) {
            for (MeetingUserEntity meetingUserEntity : meetingEntity.getMeetingUserEntityList()) {
                if (!meetingUserEntity.getUserEntity().getIsAlarmOn()) {
                    continue;
                }

                String confirmedTime = meetingUserTimetableRepository.findConfirmedPromiseTime(meetingUserEntity.getMeetingUserId());
                String confirmedPlace = meetingPlaceRepository.findConfirmedPromiseTime(meetingUserEntity.getMeetingUserId());
                pushService.send(
                        meetingUserEntity.getUserEntity().getFcmToken(),
                        new PushMessage(
                                PushMessage.ContentType.MEETING,
                                meetingEntity.getMeetingId(),
                                meetingEntity.getMeetingName(),
                                "내일은 " + confirmedTime + "에 " + confirmedPlace + "에서 약속이 있어요!")
                );
            }
        }
    }

    public void confirmRequestNoti() {
        List<MeetingEntity> meetingEntityList = getVoteNotiNeedMeetingList();

        for (MeetingEntity meetingEntity : meetingEntityList) {
            UserEntity creator = meetingEntity.getCreator();
            if (creator.getIsAlarmOn()) {
                pushService.send(creator.getFcmToken(), new PushMessage(PushMessage.ContentType.MEETING, meetingEntity.getMeetingId(), meetingEntity.getMeetingName(), "투표가 완료되었습니다. 약속을 확정해주세요!😚"));
            }
        }
    }
}
