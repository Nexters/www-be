package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
public class MeetingMainGetResDtoWrapper {
    @Getter
    public static class HostAndVotingCnt {
        private Integer votingUserCount = 0;
        private String hostName = null;
    }

    @Getter
    public static class ConfirmedPromise {
        private LocalDate promiseDate = null;
        private PromiseTime promiseTime = null;
        private String promisePlace = null;
        private HostAndVotingCnt hostAndVotingCnt = new HostAndVotingCnt();
    }

    private List<MeetingMainGetResDto> meetingMainIngGetResDtoList;
    private List<MeetingMainGetResDto> meetingMainEndGetResDtoList;

    public static MeetingMainGetResDtoWrapper of(List<MeetingEntity> meetingEntityList) {
        List<MeetingMainGetResDto> meetingMainIngGetResDtoList = new ArrayList<>();
        List<MeetingMainGetResDto> meetingMainEndGetResDtoList = new ArrayList<>();
        for (MeetingEntity meeting : meetingEntityList) {
            ConfirmedPromise confirmedPromise = new ConfirmedPromise();
            if (MeetingStatus.DONE.name().equals(meeting.getMeetingStatus())) {
                confirmedPromise = getConfirmedPromise(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainEndGetResDtoList.add(MeetingMainGetResDto.of(meeting, confirmedPromise));
            } else if (MeetingStatus.CONFIRMED.name().equals(meeting.getMeetingStatus())) {
                confirmedPromise = getConfirmedPromise(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainIngGetResDtoList.add(MeetingMainGetResDto.of(meeting, confirmedPromise));
            } else {
                confirmedPromise = getHostAndVotingCnt(meeting.getMeetingUserEntityList(), meeting.getCreator().getUserId());
                meetingMainIngGetResDtoList.add(MeetingMainGetResDto.of(meeting, confirmedPromise));
            }
        }

        return MeetingMainGetResDtoWrapper.builder()
                .meetingMainIngGetResDtoList(meetingMainIngGetResDtoList)
                .meetingMainEndGetResDtoList(meetingMainEndGetResDtoList)
                .build();
    }

    private static ConfirmedPromise getConfirmedPromise(List<MeetingUserEntity> meetingUserEntityList, Long hostId) {
        ConfirmedPromise confirmedPromise = new ConfirmedPromise();
        for (MeetingUserEntity meetingUser : meetingUserEntityList) {
            if (confirmedPromise.promiseDate == null) {
                for (MeetingUserTimetableEntity meetingUserTimetable : meetingUser.getMeetingUserTimetableEntityList()) {
                    if (meetingUserTimetable.getIsConfirmed()) {
                        confirmedPromise.promiseDate = meetingUserTimetable.getPromiseDate();
                        confirmedPromise.promiseTime = meetingUserTimetable.getPromiseTime();
                        break;
                    }
                }
            }

            if (confirmedPromise.promisePlace == null) {
                for (MeetingPlaceEntity meetingPlace : meetingUser.getMeetingPlaceEntityList()) {
                    if (meetingPlace.getIsConfirmed()) {
                        confirmedPromise.promisePlace = meetingPlace.getPromisePlace();
                        break;
                    }
                }
            }

            if (!meetingUser.getPlaceVoteEntityList().isEmpty()) {
                confirmedPromise.hostAndVotingCnt.votingUserCount++;
            }

            if (meetingUser.getMeetingUserId() == hostId) {
                confirmedPromise.hostAndVotingCnt.hostName = meetingUser.getMeetingUserName();
            }
        }

        return confirmedPromise;
    }

    private static ConfirmedPromise getHostAndVotingCnt(List<MeetingUserEntity> meetingUserEntityList, Long hostId) {
        ConfirmedPromise confirmedPromise = new ConfirmedPromise();
        for (MeetingUserEntity meetingUser : meetingUserEntityList) {
            if (!meetingUser.getPlaceVoteEntityList().isEmpty()) {
                confirmedPromise.hostAndVotingCnt.votingUserCount++;
            }

            if (meetingUser.getMeetingUserId() == hostId) {
                confirmedPromise.hostAndVotingCnt.hostName = meetingUser.getMeetingUserName();
            }
        }

        return confirmedPromise;
    }
}
