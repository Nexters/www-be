package com.promise8.wwwbe.v1.model.dto.req;

import com.promise8.wwwbe.v1.model.entity.MeetingEntityV1;
import com.promise8.wwwbe.v1.model.entity.MeetingStatusV1;
import com.promise8.wwwbe.v1.model.entity.UserEntityV1;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "MeetingCreateRequest", description = "약속방 생성에 필요한 정보를 요청한다.")
public class MeetingCreateReqDtoV1 {
    @ApiModelProperty(value = "meetingName", required = true, notes = "약속 방 이름")
    private String meetingName;
    @ApiModelProperty(value = "userName", required = true, notes = "약속 방 내에서 사용할 이름(닉네임)")
    private String userName;
    @ApiModelProperty(value = "minimumAlertMembers", required = true, notes = "알림을 위한 최소 인원")
    private Long minimumAlertMembers;
    @ApiModelProperty(value = "startDate", required = true, notes = "약속 방 시간 설정 범위 (시작)")
    private LocalDate startDate;
    @ApiModelProperty(value = "endDate", required = true, notes = "약속 방 시간 설정 범위 (끝)")
    private LocalDate endDate;
    @ApiModelProperty(value = "promiseDateTimeList", required = true, notes = "선택한 희망 날짜, 시간 list")
    private List<UserPromiseTimeReqDtoV1> promiseDateTimeList;
    @ApiModelProperty(value = "promisePlaceList", notes = "선택한 희망 장소 list")
    private List<String> promisePlaceList;


    public MeetingEntityV1 of(UserEntityV1 userEntity, String meetingCode, String shortLink) {
        return MeetingEntityV1.builder()
                .meetingName(this.meetingName)
                .conditionCount(this.minimumAlertMembers)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .meetingCode(meetingCode)
                .shortLink(shortLink)
                .meetingStatus(MeetingStatusV1.WAITING)
                .creator(userEntity)
                .build();
    }
}
