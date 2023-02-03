package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

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
    private Integer joinedUserCount;
    private LocalDate promiseDate;
    private PromiseTime promiseTime;
    private String promisePlace;
    private List<Map.Entry<MultiKey<?>, List<String>>> userPromiseDateTimeList;
    private List<UserPromisePlaceResDto> userPromisePlaceResDtoList;
    private MeetingStatus meetingStatus;
    private List<Map.Entry<String, List<String>>> userVoteList;

    public static MeetingGetResDto of(
            MeetingEntity meetingEntity,
            List<UserPromisePlaceResDto> userPromisePlaceResDtoList,
            MultiKeyMap<Object, List<String>> userPromiseTimeHashMap,
            HashMap<String, List<String>> userVoteHashMap,
            ConfirmedPromiseDto confirmedPromiseDto) {
        List<Map.Entry<String, List<String>>> userVoteList = new ArrayList<>(userVoteHashMap.entrySet());
        List<Map.Entry<MultiKey<?>, List<String>>> userPromiseDateTimeList = new ArrayList<>(userPromiseTimeHashMap.entrySet());

        Collections.sort(userVoteList, new Comparator<Map.Entry<String, List<String>>>() {
            @Override
            public int compare(Map.Entry<String, List<String>> o1, Map.Entry<String, List<String>> o2) {
                return o2.getValue().size() - o1.getValue().size();
            }
        });

        Collections.sort(userPromiseDateTimeList, new Comparator<Map.Entry<MultiKey<?>, List<String>>>() {
            @Override
            public int compare(Map.Entry<MultiKey<?>, List<String>> o1, Map.Entry<MultiKey<?>, List<String>> o2) {
                return o2.getValue().size() - o1.getValue().size();
            }
        });

        return MeetingGetResDto.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .conditionCount(meetingEntity.getConditionCount())
                .hostName(confirmedPromiseDto.getHostAndVotingCnt().getHostName())
                .joinedUserCount(meetingEntity.getMeetingUserEntityList().size())
                .promiseDate(confirmedPromiseDto.getPromiseDate())
                .promiseTime(confirmedPromiseDto.getPromiseTime())
                .promisePlace(confirmedPromiseDto.getPromisePlace())
                .userPromiseDateTimeList(userPromiseDateTimeList)
                .userPromisePlaceResDtoList(userPromisePlaceResDtoList)
                .meetingStatus(meetingEntity.getMeetingStatus())
                .userVoteList(userVoteList)
                .build();
    }
}
