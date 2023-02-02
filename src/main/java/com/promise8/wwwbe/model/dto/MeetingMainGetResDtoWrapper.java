package com.promise8.wwwbe.model.dto;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.MeetingUserTimetableEntity;
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
    private static class HostAndVotingCnt {
        private Integer votingUserCount = 0;
        private String hostName = null;
    }

    private static class ConfirmedPromise {
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
            if (MeetingStatus.DONE.name().equals(meeting.getMeetingStatus()) || MeetingStatus.CONFIRMED.name().equals(meeting.getMeetingStatus())) {
                confirmedPromise = getConfirmedPromise(meeting.getMeetingUserEntityList(), meeting.getUserEntity().getUserId());

                if (MeetingStatus.DONE.name().equals(meeting.getMeetingStatus())) {
                    meetingMainEndGetResDtoList.add(MeetingMainGetResDto.of(
                            meeting,
                            meeting.getMeetingUserEntityList(),
                            confirmedPromise.hostAndVotingCnt.votingUserCount,
                            confirmedPromise.promiseDate,
                            confirmedPromise.promiseTime,
                            confirmedPromise.promisePlace,
                            confirmedPromise.hostAndVotingCnt.hostName
                    ));
                } else {
                    meetingMainIngGetResDtoList.add(MeetingMainGetResDto.of(
                            meeting,
                            meeting.getMeetingUserEntityList(),
                            confirmedPromise.hostAndVotingCnt.votingUserCount,
                            confirmedPromise.promiseDate,
                            confirmedPromise.promiseTime,
                            confirmedPromise.promisePlace,
                            confirmedPromise.hostAndVotingCnt.hostName
                    ));
                }
            } else {
                HostAndVotingCnt hostAndVotingCnt = getHostAndVotingCnt(meeting.getMeetingUserEntityList(), meeting.getUserEntity().getUserId());
                meetingMainIngGetResDtoList.add(MeetingMainGetResDto.of(
                        meeting,
                        meeting.getMeetingUserEntityList(),
                        hostAndVotingCnt.votingUserCount,
                        null,
                        null,
                        null,
                        hostAndVotingCnt.hostName
                ));
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

            confirmedPromise.hostAndVotingCnt.votingUserCount += meetingUser.getPlaceVoteEntityList() != null && meetingUser.getPlaceVoteEntityList().size() > 0 ? 1 : 0;

            if (meetingUser.getMeetingUserId() == hostId) {
                confirmedPromise.hostAndVotingCnt.hostName = meetingUser.getMeetingUserName();
            }
        }

        return confirmedPromise;
    }

    private static HostAndVotingCnt getHostAndVotingCnt(List<MeetingUserEntity> meetingUserEntityList, Long hostId) {
        HostAndVotingCnt hostAndVotingCnt = new HostAndVotingCnt();
        for (MeetingUserEntity meetingUser : meetingUserEntityList) {
            hostAndVotingCnt.votingUserCount += meetingUser.getPlaceVoteEntityList() != null && meetingUser.getPlaceVoteEntityList().size() > 0 ? 1 : 0;

            if (meetingUser.getMeetingUserId() == hostId) {
                hostAndVotingCnt.hostName = meetingUser.getMeetingUserName();
            }
        }

        return hostAndVotingCnt;
    }
}
