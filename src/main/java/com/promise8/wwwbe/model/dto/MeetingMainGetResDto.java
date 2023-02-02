package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class MeetingMainGetResDto {
    private long meetingId;
    private String meetingName;
    private String hostName;
    private Long conditionCount;
    private Integer joinedUserCount;
    private Integer votingUserCount;
    private LocalDate promiseDate;
    private PromiseTime promiseTime;
    private String promisePlace;
    private MeetingStatus meetingStatus;

    public static MeetingMainGetResDto of(
            MeetingEntity meetingEntity,
            MeetingMainGetResDtoWrapper.ConfirmedPromise confirmedPromise) {
        return MeetingMainGetResDto.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .hostName(confirmedPromise.getHostAndVotingCnt().getHostName())
                .conditionCount(meetingEntity.getConditionCount())
                .joinedUserCount(meetingEntity.getMeetingUserEntityList().size())
                .votingUserCount(confirmedPromise.getHostAndVotingCnt().getVotingUserCount())
                .promiseDate(confirmedPromise.getPromiseDate())
                .promiseTime(confirmedPromise.getPromiseTime())
                .promisePlace(confirmedPromise.getPromisePlace())
                .meetingStatus(meetingEntity.getMeetingStatus())
                .build();
    }
}
