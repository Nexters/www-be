package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class MeetingCreateReqDto {
    private String meetingName;
    private String userName;
    private Long conditionCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String deviceId;
    private List<PromiseDateAndTimeDto> promiseDateAndTimeDtoList;
    private List<String> promisePlaceList;
    
    public MeetingEntity of(UserEntity userEntity, String meetingCode) {
        return MeetingEntity.builder()
                .meetingName(this.meetingName)
                .conditionCount(this.conditionCount)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .userEntity(userEntity)
                .meetingCode(meetingCode)
                .build();
    }
}
