package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingStatus;
import com.promise8.wwwbe.service.MeetingServiceHelper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
public class MeetingMainGetResDtoWrapper {
    private List<MeetingMainGetResDto> meetingMainIngGetResDtoList;
    private List<MeetingMainGetResDto> meetingMainEndGetResDtoList;

    public static MeetingMainGetResDtoWrapper of(List<MeetingEntity> meetingEntityList) {
        List<MeetingMainGetResDto> meetingMainIngGetResDtoList = new ArrayList<>();
        List<MeetingMainGetResDto> meetingMainEndGetResDtoList = new ArrayList<>();
        for (MeetingEntity meeting : meetingEntityList) {
            ConfirmedPromiseDto confirmedPromiseDto = new ConfirmedPromiseDto();
            if (MeetingStatus.DONE.equals(meeting.getMeetingStatus())) {
                confirmedPromiseDto = MeetingServiceHelper.getConfirmedPromise(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainEndGetResDtoList.add(MeetingMainGetResDto.of(meeting, confirmedPromiseDto));
            } else if (MeetingStatus.CONFIRMED.equals(meeting.getMeetingStatus())) {
                confirmedPromiseDto = MeetingServiceHelper.getConfirmedPromise(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainIngGetResDtoList.add(MeetingMainGetResDto.of(meeting, confirmedPromiseDto));
            } else {
                confirmedPromiseDto = MeetingServiceHelper.getHostAndVotingCnt(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainIngGetResDtoList.add(MeetingMainGetResDto.of(meeting, confirmedPromiseDto));
            }
        }

        return MeetingMainGetResDtoWrapper.builder()
                .meetingMainIngGetResDtoList(meetingMainIngGetResDtoList)
                .meetingMainEndGetResDtoList(meetingMainEndGetResDtoList)
                .build();
    }
}
