package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Setter
@Getter
@Builder
public class MeetingGetResDto {
    private long meetingId;
    private String meetingName;
    private Long conditionCount;
    private String hostName;
    private Boolean isHost;
    private Integer joinedUserCount;
    private String meetingCode;
    private String shortLink;
    private LocalDate promiseDate;
    private PromiseTime promiseTime;
    private String promisePlace;
    private List<UserPromiseTimeResDto> userPromiseTimeResDtoList;
    private List<UserPromisePlaceResDto> userPromisePlaceResDtoList;
    private MeetingStatus meetingStatus;
    private List<Map.Entry<String, List<String>>> userVoteList;

    // TODO: recentUserName 필드 추가

    public static MeetingGetResDto of(
            MeetingEntity meetingEntity,
            List<UserPromisePlaceResDto> userPromisePlaceResDtoList,
            List<UserPromiseTimeResDto> userPromiseTimeResDtoList,
            HashMap<String, List<String>> userVoteHashMap,
            ConfirmedPromiseDto confirmedPromiseDto,
            Long currentUserId) {
        List<Map.Entry<String, List<String>>> userVoteList = new ArrayList<>(userVoteHashMap.entrySet());

        Collections.sort(userVoteList, new Comparator<Map.Entry<String, List<String>>>() {
            @Override
            public int compare(Map.Entry<String, List<String>> o1, Map.Entry<String, List<String>> o2) {
                return o2.getValue().size() - o1.getValue().size();
            }
        });

        return MeetingGetResDto.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .conditionCount(meetingEntity.getConditionCount())
                .hostName(confirmedPromiseDto.getHostName())
                .isHost(meetingEntity.getCreator().getUserId() == currentUserId)
                .joinedUserCount(meetingEntity.getMeetingUserEntityList().size())
                .meetingCode(meetingEntity.getMeetingCode())
                .shortLink(meetingEntity.getShortLink())
                .promiseDate(confirmedPromiseDto.getPromiseDate())
                .promiseTime(confirmedPromiseDto.getPromiseTime())
                .promisePlace(confirmedPromiseDto.getPromisePlace())
                .userPromiseTimeResDtoList(userPromiseTimeResDtoList)
                .userPromisePlaceResDtoList(userPromisePlaceResDtoList)
                .meetingStatus(meetingEntity.getMeetingStatus())
                .userVoteList(userVoteList)
                .build();
    }
}
