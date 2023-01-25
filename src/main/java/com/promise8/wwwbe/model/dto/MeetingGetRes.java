package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
public class MeetingGetRes {
    private long meetingId;
    private String meetingName;
    private Long conditionCount;
    private String hostName;
    private Integer joinedUserCount;
    private List<UserPromiseTimeResDto> userPromiseTimeResDtoList;
    private List<UserPromisePlaceResDto> userPromisePlaceResDtoList;
    private Integer isVoting;
    private List<UserVoteResDto> userVoteResDtoList;

    public static MeetingGetRes of(
            MeetingEntity meetingEntity,
            List<MeetingUserEntity> meetingUserEntity,
            List<MeetingUserTimetableEntity> meetingUserTimetableEntityList,
            List<MeetingPlaceEntity> meetingPlaceEntityList,
            List<PlaceVoteEntity> placeVoteEntityList,
            String hostName) {
        return MeetingGetRes.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .conditionCount(meetingEntity.getConditionCount())
                .hostName(hostName)
                .joinedUserCount(meetingUserEntity.size())
                .userPromiseTimeResDtoList(
                        meetingUserTimetableEntityList.stream()
                                .map(UserPromiseTimeResDto::of).collect(Collectors.toList())
                )
                .userPromisePlaceResDtoList(
                        meetingPlaceEntityList.stream()
                                .map(UserPromisePlaceResDto::of).collect(Collectors.toList())
                )
                .isVoting(1)
                .userVoteResDtoList(
                        placeVoteEntityList.stream()
                                .map(UserVoteResDto::of).collect(Collectors.toList())
                )
                .build();
    }
}
