package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.req.PlaceVoteReqDto;
import com.promise8.wwwbe.model.dto.res.PromisePlaceResDtoWrapper;
import com.promise8.wwwbe.model.entity.*;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import com.promise8.wwwbe.model.mobile.PushMessage;
import com.promise8.wwwbe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    public void vote(long meetingId, long userId, PlaceVoteReqDto placeVoteReqDto) {
        MeetingEntity meetingEntity = getMeetingEntity(meetingId);
        UserEntity userEntity = getUserEntity(userId);
        MeetingUserEntity meetingUserEntity = getMeetingUserEntity(meetingEntity, userEntity);

        List<MeetingPlaceEntity> meetingPlaceEntityList =
                meetingPlaceRepository.findMeetingPlaceListByPlaceVoteIds(meetingUserEntity, placeVoteReqDto.getMeetingPlaceIdList());

        List<MeetingPlaceEntity> existMeetingPlaceList = meetingPlaceRepository.findByMeetingUserEntity(meetingUserEntity);

        if (!CollectionUtils.isEmpty(existMeetingPlaceList)) {
            placeVoteRepository.deleteByMeetingUserEntity(meetingUserEntity);
        }
        List<PlaceVoteEntity> placeVoteEntityList = meetingPlaceEntityList.stream()
                .map(meetingPlaceEntity -> makePlaceVoteEntity(meetingUserEntity, meetingPlaceEntity))
                .collect(Collectors.toList());
        placeVoteRepository.saveAll(placeVoteEntityList);

        List<String> userTokenList = meetingEntity.getMeetingUserEntityList().stream()
                .map(meetingUser -> meetingUser.getUserEntity().getFcmToken())
                .collect(Collectors.toList());

        int meetingUserSize = meetingEntity.getMeetingUserEntityList().size();
        int votedUserCount = placeVoteRepository.getVotedUserCount(meetingId);
        if (meetingUserSize == votedUserCount) {
            for (String token : userTokenList) {
                pushService.send(token, new PushMessage(PushMessage.ContentType.MEETING, meetingId, "장소 선정 투표가 완료되었어요.\n투표 결과를 확인해보세요!"));
            }
            meetingEntity.setMeetingStatus(MeetingStatus.VOTED);
            meetingRepository.save(meetingEntity);
        }
    }

    private MeetingUserEntity getMeetingUserEntity(MeetingEntity meetingEntity, UserEntity userEntity) {
        MeetingUserEntity meetingUserEntity = meetingUserRepository.findByMeetingEntityAndUserEntity(meetingEntity, userEntity).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.NOT_EXIST_MEETING_USER);
        });
        return meetingUserEntity;
    }

    private UserEntity getUserEntity(long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));
        return userEntity;
    }

    private MeetingEntity getMeetingEntity(long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.NOT_EXIST_MEETING);
        });
        return meetingEntity;
    }

    private PlaceVoteEntity makePlaceVoteEntity(MeetingUserEntity meetingUserEntity, MeetingPlaceEntity meetingPlaceEntity) {
        PlaceVoteEntity placeVoteEntity = PlaceVoteEntity.builder()
                .meetingPlaceEntity(meetingPlaceEntity)
                .meetingUserEntity(meetingUserEntity)
                .build();
        return placeVoteEntity;
    }

    public PromisePlaceResDtoWrapper getMeetingPlaceList(Long userId, Long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_USER));
        MeetingUserEntity meetingUser = meetingUserRepository.findByMeetingEntityAndUserEntity(meetingEntity, userEntity)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING_USER));

        HashMap<String, List<String>> userVoteHashMap = MeetingServiceHelper.getUserVoteHashMap(meetingEntity);
        List<String> myVoteList = getMyVoteList(userVoteHashMap, meetingUser.getMeetingUserName());

        return PromisePlaceResDtoWrapper.of(userVoteHashMap, myVoteList);
    }

    private List<String> getMyVoteList(HashMap<String, List<String>> userVoteHashMap, String myName) {
        List<String> myVoteList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry: userVoteHashMap.entrySet()) {
            String place = entry.getKey();
            for (String userName: entry.getValue()) {
                if (myName.equals(userName)) {
                    myVoteList.add(place);
                }
            }
        }

        return myVoteList;
    }
}
