package com.promise8.wwwbe.model.v1.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promise8.wwwbe.model.v1.dto.PromiseDayOfWeek;
import com.promise8.wwwbe.service.MeetingServiceHelper;
import com.promise8.wwwbe.service.ThumbnailHelper;
import com.promise8.wwwbe.model.v1.dto.PromiseTime;
import com.promise8.wwwbe.model.v1.entity.MeetingEntityV1;
import com.promise8.wwwbe.model.v1.entity.MeetingStatusV1;
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
public class MeetingMainGetResDtoV1 {
    @ApiModelProperty(value = "meetingId", required = true, notes = "약속 방 id")
    private long meetingId;
    @ApiModelProperty(value = "meetingName", required = true, notes = "약속 방 이름")
    private String meetingName;
    @ApiModelProperty(value = "hostName", required = true, notes = "약속 방의 방장 이름")
    private String hostName;
    @ApiModelProperty(value = "minimumAlertMembers", required = true, notes = "약속 방의 알림 최소 인원")
    private Long minimumAlertMembers;
    @ApiModelProperty(value = "joinedUserCount", required = true, notes = "약속 방에 참여한 인원 수")
    private Integer joinedUserCount;
    @ApiModelProperty(value = "votingUserCount", required = true, notes = "약속 방 내 투표한 인원 수")
    private Integer votingUserCount;
    @ApiModelProperty(value = "confirmedDate", notes = "확정된 날짜")
    private LocalDate confirmedDate;
    @ApiModelProperty(value = "confirmedDayOfWeek", notes = "요일")
    private PromiseDayOfWeek confirmedDayOfWeek;
    @ApiModelProperty(value = "confirmedTime", notes = "확정된 시간대")
    private PromiseTime confirmedTime;
    @ApiModelProperty(value = "confirmedPlace", notes = "확정된 장소")
    private String confirmedPlace;
    @ApiModelProperty(value = "meetingStatus", required = true, notes = "약속 방 상태")
    private MeetingStatusV1 meetingStatus;
    @JsonIgnore
    private LocalDateTime createdDatetime;
    @ApiModelProperty(value = "yaksokiType", required = true, notes = "캐릭터 이미지 타입")
    private ThumbnailHelper.YaksokiType yaksokiType;

    public static MeetingMainGetResDtoV1 of(
            MeetingEntityV1 meetingEntity,
            ConfirmedPromiseResDtoV1 confirmedPromiseResDto) {
        boolean isNoMeetingUser = meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty();
        return MeetingMainGetResDtoV1.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .hostName(confirmedPromiseResDto.getHostName())
                .minimumAlertMembers(meetingEntity.getConditionCount())
                .joinedUserCount(isNoMeetingUser ? 0 : meetingEntity.getMeetingUserEntityList().size())
                .votingUserCount(confirmedPromiseResDto.getVotingUserCount())
                .confirmedDate(confirmedPromiseResDto.getPromiseDate())
                .confirmedDayOfWeek(MeetingServiceHelper.getPromiseDayOfWeek(confirmedPromiseResDto.getPromiseDate()))
                .confirmedTime(confirmedPromiseResDto.getPromiseTime())
                .confirmedPlace(confirmedPromiseResDto.getPromisePlace())
                .meetingStatus(meetingEntity.getMeetingStatus())
                .createdDatetime(meetingEntity.getCreatedDatetime())
                .yaksokiType(ThumbnailHelper.getYaksoki(meetingEntity.getMeetingId()))
                .build();
    }
}
