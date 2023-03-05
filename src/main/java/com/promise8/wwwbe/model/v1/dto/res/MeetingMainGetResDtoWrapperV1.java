package com.promise8.wwwbe.model.v1.dto.res;

import com.promise8.wwwbe.service.MeetingServiceHelper;
import com.promise8.wwwbe.model.v1.entity.MeetingEntityV1;
import com.promise8.wwwbe.model.v1.entity.MeetingStatusV1;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@ApiModel(value = "MeetingMainList", description = "본인이 참여한 MeetingList이며, 메인 Home 화면에 보여진다.")
public class MeetingMainGetResDtoWrapperV1 {
    @ApiModelProperty(value = "meetingIngList", notes = "약속이 진행중인 MeetingList")
    private List<MeetingMainGetResDtoV1> meetingIngList;
    @ApiModelProperty(value = "meetingEndList", notes = "약속이 종료된 MeetingList")
    private List<MeetingMainGetResDtoV1> meetingEndList;

    public static MeetingMainGetResDtoWrapperV1 of(List<MeetingEntityV1> meetingEntityList) {
        List<MeetingMainGetResDtoV1> meetingMainIngGetResDtoList = new ArrayList<>();
        List<MeetingMainGetResDtoV1> meetingMainEndGetResDtoList = new ArrayList<>();
        for (MeetingEntityV1 meeting : meetingEntityList) {
            ConfirmedPromiseResDtoV1 confirmedPromiseResDto = new ConfirmedPromiseResDtoV1();
            if (MeetingStatusV1.DONE.equals(meeting.getMeetingStatus())) {
                confirmedPromiseResDto = MeetingServiceHelper.getConfirmedPromise(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainEndGetResDtoList.add(MeetingMainGetResDtoV1.of(meeting, confirmedPromiseResDto));
            } else if (MeetingStatusV1.CONFIRMED.equals(meeting.getMeetingStatus())) {
                confirmedPromiseResDto = MeetingServiceHelper.getConfirmedPromise(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainIngGetResDtoList.add(MeetingMainGetResDtoV1.of(meeting, confirmedPromiseResDto));
            } else {
                confirmedPromiseResDto = MeetingServiceHelper.getHostAndVotingCnt(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainIngGetResDtoList.add(MeetingMainGetResDtoV1.of(meeting, confirmedPromiseResDto));
            }
        }

        meetingMainIngGetResDtoList.sort(MeetingMainGetResDtoWrapperV1::sorting);
        return MeetingMainGetResDtoWrapperV1.builder()
                .meetingIngList(meetingMainIngGetResDtoList)
                .meetingEndList(meetingMainEndGetResDtoList)
                .build();
    }

    private static int sorting(MeetingMainGetResDtoV1 o1, MeetingMainGetResDtoV1 o2) {
        if (o1.getMeetingStatus().equals(o2.getMeetingStatus())) {
            if (MeetingStatusV1.CONFIRMED.equals(o1.getMeetingStatus())) {
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
