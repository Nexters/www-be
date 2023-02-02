package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import com.promise8.wwwbe.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class MeetingCreateReqDto {
    private String meetingName;
    private String userName;
    private Long conditionCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String deviceId;
    private List<PromiseDateAndTimeReqDto> promiseDateAndTimeReqDtoList;
    private List<String> promisePlaceList;
    private PlatformType platformType;

    public MeetingEntity of(UserEntity userEntity, String meetingCode) {
        return MeetingEntity.builder()
                .meetingName(this.meetingName)
                .conditionCount(this.conditionCount)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .meetingCode(meetingCode)
                .meetingStatus(MeetingStatus.WAITING)
                .creator(userEntity)
                .build();
    }
}
