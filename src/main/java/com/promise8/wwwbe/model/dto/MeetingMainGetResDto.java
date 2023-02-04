package com.promise8.wwwbe.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @JsonIgnore
    private LocalDateTime createdDatetime;

    public static MeetingMainGetResDto of(
            MeetingEntity meetingEntity,
            ConfirmedPromiseDto confirmedPromiseDto) {
        return MeetingMainGetResDto.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .hostName(confirmedPromiseDto.getHostName())
                .conditionCount(meetingEntity.getConditionCount())
                .joinedUserCount(meetingEntity.getMeetingUserEntityList().size())
                .votingUserCount(confirmedPromiseDto.getVotingUserCount())
                .promiseDate(confirmedPromiseDto.getPromiseDate())
                .promiseTime(confirmedPromiseDto.getPromiseTime())
                .promisePlace(confirmedPromiseDto.getPromisePlace())
                .meetingStatus(meetingEntity.getMeetingStatus())
                .createdDatetime(meetingEntity.getCreatedDatetime())
                .build();
    }
}
