package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@Builder
public class MeetingGetRes {
    private long meetingId;
    private String meetingName;
    private Long conditionCount;
    private String hostName;
    private Integer joinedUserCount;
    private LocalDate promiseDate;
    private PromiseTime promiseTime;
    private String promisePlace;
    private HashMap<LocalDate, List<String[]>> userPromiseTimeHashMap;
    private List<UserPromisePlaceResDto> userPromisePlaceResDtoList;
    private Integer isVoting;
    private HashMap<String, List<String>> userVoteHashMap;

    public static MeetingGetRes of(
            MeetingEntity meetingEntity,
            List<UserPromisePlaceResDto> userPromisePlaceResDtoList,
            HashMap<LocalDate, List<String[]>> userPromiseTimeHashMap,
            HashMap<String, List<String>> userVoteHashMap) {
        return MeetingGetRes.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .conditionCount(meetingEntity.getConditionCount())
                .hostName(meetingEntity.getUserEntity().getUserName())
                .joinedUserCount(meetingEntity.getMeetingUserEntityList().size())
                .userPromiseTimeHashMap(userPromiseTimeHashMap)
                .userPromisePlaceResDtoList(userPromisePlaceResDtoList)
                .isVoting(1)
                .userVoteHashMap(userVoteHashMap)
                .build();
    }
}
