package com.promise8.wwwbe.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ApiModel(value = "MeetingMain", description = "본인이 참여한 하나의 Meeting 정보, 메인 Home 화면에 보여진다.")
public class MeetingMainGetResDto {
    @ApiModelProperty(value = "meetingId", notes = "약속 방 id")
    private long meetingId;
    @ApiModelProperty(value = "meetingName", notes = "약속 방 이름")
    private String meetingName;
    @ApiModelProperty(value = "hostName", notes = "약속 방의 방장 이름")
    private String hostName;
    @ApiModelProperty(value = "conditionCount", notes = "약속 방의 알림 최소 인원")
    private Long conditionCount;
    @ApiModelProperty(value = "joinedUserCount", notes = "약속 방에 참여한 인원 수")
    private Integer joinedUserCount;
    @ApiModelProperty(value = "votingUserCount", notes = "약속 방 내 투표한 인원 수")
    private Integer votingUserCount;
    @ApiModelProperty(value = "confirmedDate", notes = "확정된 날짜")
    private LocalDate confirmedDate;
    @ApiModelProperty(value = "confirmedTime", notes = "확정된 시간대")
    private PromiseTime confirmedTime;
    @ApiModelProperty(value = "confirmedPlace", notes = "확정된 장소")
    private String confirmedPlace;
    @ApiModelProperty(value = "meetingStatus", notes = "약속 방 상태")
    private MeetingStatus meetingStatus;
    @JsonIgnore
    private LocalDateTime createdDatetime;

    public static MeetingMainGetResDto of(
            MeetingEntity meetingEntity,
            ConfirmedPromiseDto confirmedPromiseDto) {
        boolean isNoMeetingUser = meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty();
        return MeetingMainGetResDto.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .hostName(confirmedPromiseDto.getHostName())
                .conditionCount(meetingEntity.getConditionCount())
                .joinedUserCount(isNoMeetingUser ? 0 : meetingEntity.getMeetingUserEntityList().size())
                .votingUserCount(confirmedPromiseDto.getVotingUserCount())
                .confirmedDate(confirmedPromiseDto.getPromiseDate())
                .confirmedTime(confirmedPromiseDto.getPromiseTime())
                .confirmedPlace(confirmedPromiseDto.getPromisePlace())
                .meetingStatus(meetingEntity.getMeetingStatus())
                .createdDatetime(meetingEntity.getCreatedDatetime())
                .build();
    }
}
