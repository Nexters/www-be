package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import com.promise8.wwwbe.model.entity.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@ApiModel(value = "MeetingCreateRequest", description = "약속방 생성에 필요한 정보를 요청한다.")
public class MeetingCreateReqDto {
    @ApiModelProperty(value = "meetingName", required = true, notes = "약속 방 이름")
    private String meetingName;
    @ApiModelProperty(value = "userName", required = true, notes = "약속 방 내에서 사용할 이름(닉네임)")
    private String userName;
    @ApiModelProperty(value = "conditionCount", required = true, notes = "알림을 위한 최소 인원")
    private Long conditionCount;
    @ApiModelProperty(value = "startDate", required = true, notes = "약속 방 시간 설정 범위 (시작)")
    private LocalDate startDate;
    @ApiModelProperty(value = "endDate", required = true, notes = "약속 방 시간 설정 범위 (끝)")
    private LocalDate endDate;
    @ApiModelProperty(value = "promiseDateTimeList", required = true, notes = "선택한 희망 날짜, 시간 list")
    private List<PromiseDateTimeReqDto> promiseDateTimeList;
    @ApiModelProperty(value = "promisePlaceList", notes = "선택한 희망 장소 list")
    private List<String> promisePlaceList;
    @ApiModelProperty(value = "platformType", required = true, notes = "현재 사용중인 플랫폼")
    private PlatformType platformType;


    public MeetingEntity of(UserEntity userEntity, String meetingCode, String shortLink) {
        return MeetingEntity.builder()
                .meetingName(this.meetingName)
                .conditionCount(this.conditionCount)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .meetingCode(meetingCode)
                .shortLink(shortLink)
                .meetingStatus(MeetingStatus.WAITING)
                .creator(userEntity)
                .build();
    }
}
