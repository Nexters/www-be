package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.v1.entity.*;
import com.promise8.wwwbe.repository.*;
import com.promise8.wwwbe.model.v1.dto.req.PlaceVoteReqDtoV1;
import com.promise8.wwwbe.model.v1.dto.res.PromisePlaceResDtoWrapperV1;
import com.promise8.wwwbe.model.v1.exception.BizException;
import com.promise8.wwwbe.model.v1.http.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceVoteService {
    private final PushService pushService;
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final MeetingPlaceRepository meetingPlaceRepository;
    private final UserRepository userRepository;
    private final PlaceVoteRepository placeVoteRepository;

    @Transactional
    public void vote(long meetingId, long userId, PlaceVoteReqDtoV1 placeVoteReqDto) {
        MeetingEntityV1 meetingEntity = getMeetingEntity(meetingId);
        UserEntityV1 userEntity = getUserEntity(userId);
        MeetingUserEntityV1 meetingUserEntity = getMeetingUserEntity(meetingEntity, userEntity);

        List<MeetingPlaceEntityV1> meetingPlaceEntityList =
                meetingPlaceRepository.findMeetingPlaceListByPlaceVoteIds(placeVoteReqDto.getMeetingPlaceIdList());

        List<MeetingPlaceEntityV1> existMeetingPlaceList = meetingPlaceRepository.findByMeetingUserEntity(meetingUserEntity);

        if (!CollectionUtils.isEmpty(existMeetingPlaceList)) {
            placeVoteRepository.deleteByMeetingUserEntity(meetingUserEntity);
        }
        List<PlaceVoteEntityV1> placeVoteEntityList = meetingPlaceEntityList.stream()
                .map(meetingPlaceEntity -> makePlaceVoteEntity(meetingUserEntity, meetingPlaceEntity))
                .collect(Collectors.toList());
        placeVoteRepository.saveAll(placeVoteEntityList);

//        List<UserEntityV1> userEntityList = meetingEntity.getMeetingUserEntityList().stream()
//                .map(MeetingUserEntityV1::getUserEntity)
//                .collect(Collectors.toList());

        int meetingUserSize = meetingEntity.getMeetingUserEntityList().size();
        int votedUserCount = placeVoteRepository.getVotedUserCount(meetingId);
        if (meetingUserSize == votedUserCount) {
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
            meetingEntity.setVoteFinishDateTime(LocalDateTime.now());
            meetingEntity.setMeetingStatus(MeetingStatusV1.VOTED);
            meetingRepository.save(meetingEntity);
        }
    }

    private MeetingUserEntityV1 getMeetingUserEntity(MeetingEntityV1 meetingEntity, UserEntityV1 userEntity) {
        MeetingUserEntityV1 meetingUserEntity = meetingUserRepository.findByMeetingEntityAndUserEntity(meetingEntity, userEntity).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.NOT_EXIST_MEETING_USER);
        });
        return meetingUserEntity;
    }

    private UserEntityV1 getUserEntity(long userId) {
        UserEntityV1 userEntity = userRepository.findById(userId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));
        return userEntity;
    }

    private MeetingEntityV1 getMeetingEntity(long meetingId) {
        MeetingEntityV1 meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.NOT_EXIST_MEETING);
        });
        return meetingEntity;
    }

    private PlaceVoteEntityV1 makePlaceVoteEntity(MeetingUserEntityV1 meetingUserEntity, MeetingPlaceEntityV1 meetingPlaceEntity) {
        PlaceVoteEntityV1 placeVoteEntity = PlaceVoteEntityV1.builder()
                .meetingPlaceEntity(meetingPlaceEntity)
                .meetingUserEntity(meetingUserEntity)
                .build();
        return placeVoteEntity;
    }

    public PromisePlaceResDtoWrapperV1 getMeetingPlaceList(Long userId, Long meetingId) {
        MeetingEntityV1 meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        UserEntityV1 userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));
        MeetingUserEntityV1 meetingUser = meetingUserRepository.findByMeetingEntityAndUserEntity(meetingEntity, userEntity)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING_USER));
        int votedUserCount = placeVoteRepository.getVotedUserCount(meetingId);

        HashMap<String, List<String>> userVoteHashMap = MeetingServiceHelper.getUserVoteHashMap(meetingEntity);
        List<String> myVoteList = getMyVoteList(userVoteHashMap, meetingUser.getMeetingUserName());

        return PromisePlaceResDtoWrapperV1.of(userVoteHashMap, myVoteList, votedUserCount);
    }

    private List<String> getMyVoteList(HashMap<String, List<String>> userVoteHashMap, String myName) {
        List<String> myVoteList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : userVoteHashMap.entrySet()) {
            String place = entry.getKey();
            for (String userName : entry.getValue()) {
                if (myName.equals(userName)) {
                    myVoteList.add(place);
                }
            }
        }

        return myVoteList;
    }
}
