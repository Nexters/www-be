package com.promise8.wwwbe.model.dto.res;

import com.promise8.wwwbe.model.dto.PromiseTime;
import com.promise8.wwwbe.model.dto.UserInfoDto;
import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.UserEntity;
import com.promise8.wwwbe.service.ThumbnailHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Builder
@ApiModel(value = "MeetingDetail", description = "약속 방 내의 필요한 정보를 모두 담는다.")
public class MeetingGetResDto {
    @ApiModelProperty(value = "meetingId", required = true, notes = "약속 방 id")
    private long meetingId;
    @ApiModelProperty(value = "meetingName", required = true, notes = "약속 방 이름")
    private String meetingName;
    @ApiModelProperty(value = "minimumAlertMembers", required = true, notes = "약속 방의 알림 최소 인원")
    private Long minimumAlertMembers;
    @ApiModelProperty(value = "hostName", required = true, notes = "약속 방의 방장 이름")
    private String hostName;
    @ApiModelProperty(value = "currentUserName", notes = "최근에 사용한 본인의 이름")
    private String currentUserName;
    @ApiModelProperty(value = "isHost", required = true, notes = "약속 방에서 내가 방장인지 여부")
    private Boolean isHost;
    @ApiModelProperty(value = "joinedUserCount", required = true, notes = "약속 방에 참여한 인원 수")
    private Integer joinedUserCount;
    @ApiModelProperty(value = "votingUserCount", required = true, notes = "약속 방 내 투표한 인원 수")
    private Integer votingUserCount;
    @ApiModelProperty(value = "meetingCode", required = true, notes = "약속 방의 공유 코드")
    private String meetingCode;
    @ApiModelProperty(value = "shortLink", required = true, notes = "약속 방의 공유 링크")
    private String shortLink;
    @ApiModelProperty(value = "confirmedDate", notes = "확정된 날짜")
    private LocalDate confirmedDate;
    @ApiModelProperty(value = "confirmedTime", notes = "확정된 시간대")
    private PromiseTime confirmedTime;
    @ApiModelProperty(value = "confirmedPlace", notes = "확정된 장소")
    private String confirmedPlace;
    @ApiModelProperty(value = "isJoined", required = true, notes = "이미 방에 참여했는지 여부")
    private Boolean isJoined;
    @ApiModelProperty(value = "startDate", required = true, notes = "약속 방 시간(시작)")
    private LocalDate startDate;
    @ApiModelProperty(value = "endDate", required = true, notes = "약속 방 시간(끝)")
    private LocalDate endDate;
    @ApiModelProperty(value = "yaksokiType", required = true, notes = "캐릭터 이미지 타입")
    private ThumbnailHelper.YaksokiType yaksokiType;

    @ApiModelProperty(value = "joinedUserInfoList", required = true, notes = "약속 방 내 이미 참여한 유저닉네임 리스트(캐릭터 타입 포함)")
    private List<UserInfoDto> joinedUserInfoList;
    @ApiModelProperty(value = "userPromiseDateTimeList", required = true, notes = "약속 방 내 유저들이 희망하는 날짜, 시간대")
    private List<UserPromiseTimeResDto> userPromiseDateTimeList;
    @ApiModelProperty(value = "userPromisePlaceList", notes = "약속 방 내 유저들이 희망하는 장소")
    private List<UserPromisePlaceResDto> userPromisePlaceList;
    @ApiModelProperty(value = "meetingStatus", required = true, notes = "약속 방 상태")
    private MeetingStatus meetingStatus;
    @ApiModelProperty(value = "userVoteList", notes = "약속 방 내 유저들의 투표 내역")
    private List<Map.Entry<String, List<String>>> userVoteList;

    public static MeetingGetResDto of(
            MeetingEntity meetingEntity,
            List<UserInfoDto> userInfoDtoList,
            List<UserPromisePlaceResDto> userPromisePlaceResDtoList,
            List<UserPromiseTimeResDto> userPromiseTimeResDtoList,
            HashMap<String, List<String>> userVoteHashMap,
            ConfirmedPromiseResDto confirmedPromiseResDto,
            UserEntity userEntity,
            Boolean isJoined) {
        List<Map.Entry<String, List<String>>> userVoteList = new ArrayList<>(userVoteHashMap.entrySet());
        userVoteList.sort((o1, o2) -> o2.getValue().size() - o1.getValue().size());

        String currentUserName = null;
        if (meetingEntity.getMeetingUserEntityList() != null) {
            for (MeetingUserEntity meetingUserEntity : meetingEntity.getMeetingUserEntityList()) {
                if (userEntity.getUserId().equals(meetingUserEntity.getUserEntity().getUserId())) {
                    currentUserName = meetingUserEntity.getMeetingUserName();
                    break;
                }
            }
        }

        boolean isNoMeetingUser = meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty();
        return MeetingGetResDto.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .minimumAlertMembers(meetingEntity.getConditionCount())
                .hostName(confirmedPromiseResDto.getHostName())
                .currentUserName(currentUserName)
                .isHost(userEntity.getUserId().equals(meetingEntity.getCreator().getUserId()))
                .joinedUserCount(isNoMeetingUser ? 0 : meetingEntity.getMeetingUserEntityList().size())
                .votingUserCount(confirmedPromiseResDto.getVotingUserCount())
                .meetingCode(meetingEntity.getMeetingCode())
                .shortLink(meetingEntity.getShortLink())
                .confirmedDate(confirmedPromiseResDto.getPromiseDate())
                .confirmedTime(confirmedPromiseResDto.getPromiseTime())
                .confirmedPlace(confirmedPromiseResDto.getPromisePlace())
                .isJoined(isJoined)
                .startDate(meetingEntity.getStartDate())
                .endDate(meetingEntity.getEndDate())
                .joinedUserInfoList(userInfoDtoList)
                .userPromiseDateTimeList(userPromiseTimeResDtoList)
                .userPromisePlaceList(userPromisePlaceResDtoList)
                .meetingStatus(meetingEntity.getMeetingStatus())
                .userVoteList(userVoteList)
                .yaksokiType(ThumbnailHelper.getYaksoki(meetingEntity.getMeetingId()))
                .build();
    }
}
