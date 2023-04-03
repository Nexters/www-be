package com.promise8.wwwbe.model.v2.dto.res;

import com.promise8.wwwbe.model.v1.dto.res.ConfirmedPromiseResDtoV1;
import com.promise8.wwwbe.model.v1.entity.MeetingEntityV1;
import com.promise8.wwwbe.model.v1.entity.MeetingStatusV1;
import com.promise8.wwwbe.model.v1.entity.MeetingUserEntityV1;
import com.promise8.wwwbe.model.v1.entity.UserEntityV1;
import com.promise8.wwwbe.service.MeetingServiceHelper;
import com.promise8.wwwbe.service.ThumbnailHelper;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@ApiModel(value = "MeetingDetail", description = "약속방 내의 필요한 정보를 모두 담는다.")
public class MeetingGetResDtoV2 {
    private Long meetingId;
    private String meetingName;
    private int minimumAlertMembers;
    private String currentUserName;
    private Boolean isHost;
    private int joinedCount;
    private int votingCount;
    private String meetingCode;
    private String shortLink;
    private ConfirmedPromise confirmedPromise;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<UserInfoDtoV2> joinedUserInfoList;
    private List<UserPromiseTimeResDtoV2> promiseTimeList;
    private List<UserPromisePlaceResDtoV2> promisePlaceList;
    private MeetingStatusV1 meetingStatus;
    private ThumbnailHelper.YaksokiType yaksokiType;

    public static MeetingGetResDtoV2 of(
            MeetingEntityV1 meetingEntity,
            List<UserInfoDtoV2> userInfoDtoList,
            List<UserPromisePlaceResDtoV2> userPromisePlaceResDtoList,
            List<UserPromiseTimeResDtoV2> userPromiseTimeResDtoList,
            ConfirmedPromiseResDtoV1 confirmedPromiseResDto,
            UserEntityV1 userEntity) {

        String currentUserName = null;
        if (meetingEntity.getMeetingUserEntityList() != null) {
            for (MeetingUserEntityV1 meetingUserEntity : meetingEntity.getMeetingUserEntityList()) {
                if (userEntity.getUserId().equals(meetingUserEntity.getUserEntity().getUserId())) {
                    currentUserName = meetingUserEntity.getMeetingUserName();
                    break;
                }
            }
        }

        ConfirmedPromise confirmedPromise = ConfirmedPromise.builder()
                .promiseDate(confirmedPromiseResDto.getPromiseDate())
                .promiseDayOfWeek(MeetingServiceHelper.getPromiseDayOfWeek(confirmedPromiseResDto.getPromiseDate()))
                .promiseTime(confirmedPromiseResDto.getPromiseTime())
                .promisePlace(confirmedPromiseResDto.getPromisePlace())
                .build();

        boolean isNoMeetingUser = meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty();
        return MeetingGetResDtoV2.builder()
                .meetingId(meetingEntity.getMeetingId())
                .meetingName(meetingEntity.getMeetingName())
                .minimumAlertMembers(meetingEntity.getConditionCount().intValue())
                .currentUserName(currentUserName == null ? userEntity.getUserName() : currentUserName)
                .isHost(userEntity.getUserId().equals(meetingEntity.getCreator().getUserId()))
                .joinedCount(isNoMeetingUser ? 0 : meetingEntity.getMeetingUserEntityList().size())
                .votingCount(confirmedPromiseResDto.getVotingUserCount())
                .meetingCode(meetingEntity.getMeetingCode())
                .shortLink(meetingEntity.getShortLink())
                .confirmedPromise(confirmedPromise)
                .startDate(meetingEntity.getStartDate())
                .endDate(meetingEntity.getEndDate())
                .joinedUserInfoList(userInfoDtoList)
                .promiseTimeList(userPromiseTimeResDtoList)
                .promisePlaceList(userPromisePlaceResDtoList)
                .meetingStatus(meetingEntity.getMeetingStatus())
                .yaksokiType(ThumbnailHelper.getYaksoki(meetingEntity.getMeetingId()))
                .build();
    }
}
