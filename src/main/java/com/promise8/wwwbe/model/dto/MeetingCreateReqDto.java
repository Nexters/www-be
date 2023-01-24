package com.promise8.wwwbe.model.dto;

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
}
