package com.promise8.wwwbe.model.dto.res;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import com.promise8.wwwbe.service.MeetingServiceHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Setter
@Getter
@Builder
@ApiModel(value = "MeetingMainList", description = "본인이 참여한 MeetingList이며, 메인 Home 화면에 보여진다.")
public class MeetingMainGetResDtoWrapper {
    @ApiModelProperty(value = "meetingIngList", notes = "약속이 진행중인 MeetingList")
    private List<MeetingMainGetResDto> meetingIngList;
    @ApiModelProperty(value = "meetingEndList", notes = "약속이 종료된 MeetingList")
    private List<MeetingMainGetResDto> meetingEndList;

    public static MeetingMainGetResDtoWrapper of(List<MeetingEntity> meetingEntityList) {
        List<MeetingMainGetResDto> meetingMainIngGetResDtoList = new ArrayList<>();
        List<MeetingMainGetResDto> meetingMainEndGetResDtoList = new ArrayList<>();
        for (MeetingEntity meeting : meetingEntityList) {
            ConfirmedPromiseResDto confirmedPromiseResDto = new ConfirmedPromiseResDto();
            if (MeetingStatus.DONE.equals(meeting.getMeetingStatus())) {
                confirmedPromiseResDto = MeetingServiceHelper.getConfirmedPromise(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainEndGetResDtoList.add(MeetingMainGetResDto.of(meeting, confirmedPromiseResDto));
            } else if (MeetingStatus.CONFIRMED.equals(meeting.getMeetingStatus())) {
                confirmedPromiseResDto = MeetingServiceHelper.getConfirmedPromise(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainIngGetResDtoList.add(MeetingMainGetResDto.of(meeting, confirmedPromiseResDto));
            } else {
                confirmedPromiseResDto = MeetingServiceHelper.getHostAndVotingCnt(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainIngGetResDtoList.add(MeetingMainGetResDto.of(meeting, confirmedPromiseResDto));
            }
        }

        meetingMainIngGetResDtoList.sort(MeetingMainGetResDtoWrapper::sorting);
        return MeetingMainGetResDtoWrapper.builder()
                .meetingIngList(meetingMainIngGetResDtoList)
                .meetingEndList(meetingMainEndGetResDtoList)
                .build();
    }

    private static int sorting(MeetingMainGetResDto o1, MeetingMainGetResDto o2) {
        if (o1.getMeetingStatus().equals(o2.getMeetingStatus())) {
            if (MeetingStatus.CONFIRMED.equals(o1.getMeetingStatus())) {
                if (o1.getConfirmedDate().equals(o2.getConfirmedDate())) {
                    if (o1.getConfirmedTime().equals(o2.getConfirmedTime())) {
                        return o2.getCreatedDatetime().compareTo(o1.getCreatedDatetime());
                    } else {
                        return o1.getConfirmedTime().getPriority() - o2.getConfirmedTime().getPriority();
                    }
                } else {
                    return o1.getConfirmedTime().getPriority() - o2.getConfirmedTime().getPriority();
                }
            } else {
                return o2.getCreatedDatetime().compareTo(o1.getCreatedDatetime());
            }
        } else {
            return o2.getMeetingStatus().getPriority() - o1.getMeetingStatus().getPriority();
        }
    }
}
