package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
            List<MeetingUserEntity> meetingUserEntityList,
            Integer votingUserCount,
            LocalDate promiseDate,
            PromiseTime promiseTime,
            String promisePlace,
            String hostName) {
        return MeetingMainGetResDto.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .hostName(hostName)
                .conditionCount(meetingEntity.getConditionCount())
                .joinedUserCount(meetingUserEntityList.size())
                .votingUserCount(votingUserCount)
                .promiseDate(promiseDate)
                .promiseTime(promiseTime)
                .promisePlace(promisePlace)
                .meetingStatus(MeetingStatus.valueOf(meetingEntity.getMeetingStatus()))
                .build();
    }
}
