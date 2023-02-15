package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.res.ConfirmedPromiseResDto;
import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.MeetingUserTimetableEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeetingServiceHelper {
    public static ConfirmedPromiseResDto getConfirmedPromise(List<MeetingUserEntity> meetingUserEntityList, Long hostId) {
        ConfirmedPromiseResDto confirmedPromiseResDto = new ConfirmedPromiseResDto();
        if (meetingUserEntityList == null || meetingUserEntityList.isEmpty()) return confirmedPromiseResDto;

        for (MeetingUserEntity meetingUser : meetingUserEntityList) {
            if (confirmedPromiseResDto.getPromiseDate() == null) {
                for (MeetingUserTimetableEntity meetingUserTimetable : meetingUser.getMeetingUserTimetableEntityList()) {
                    if (meetingUserTimetable.getIsConfirmed()) {
                        confirmedPromiseResDto.setPromiseDate(meetingUserTimetable.getPromiseDate());
                        confirmedPromiseResDto.setPromiseTime(meetingUserTimetable.getPromiseTime());
                        break;
                    }
                }
            }

            if (confirmedPromiseResDto.getPromisePlace() == null) {
                for (MeetingPlaceEntity meetingPlace : meetingUser.getMeetingPlaceEntityList()) {
                    if (meetingPlace.getIsConfirmed()) {
                        confirmedPromiseResDto.setPromisePlace(meetingPlace.getPromisePlace());
                        break;
                    }
                }
            }

            if (!meetingUser.getPlaceVoteEntityList().isEmpty()) {
                confirmedPromiseResDto.setVotingUserCount(confirmedPromiseResDto.getVotingUserCount() + 1);
            }

            if (meetingUser.getUserEntity().getUserId() == hostId) {
                confirmedPromiseResDto.setHostName(meetingUser.getMeetingUserName());
            }
        }

        return confirmedPromiseResDto;
    }

    public static ConfirmedPromiseResDto getHostAndVotingCnt(List<MeetingUserEntity> meetingUserEntityList, Long hostId) {
        ConfirmedPromiseResDto confirmedPromiseResDto = new ConfirmedPromiseResDto();
        if (meetingUserEntityList == null || meetingUserEntityList.isEmpty()) return confirmedPromiseResDto;

        for (MeetingUserEntity meetingUser : meetingUserEntityList) {
            if (!meetingUser.getPlaceVoteEntityList().isEmpty()) {
                confirmedPromiseResDto.setVotingUserCount(confirmedPromiseResDto.getVotingUserCount() + 1);
            }

            if (meetingUser.getUserEntity().getUserId() == hostId) {
                confirmedPromiseResDto.setHostName(meetingUser.getMeetingUserName());
            }
        }

        return confirmedPromiseResDto;
    }
}
