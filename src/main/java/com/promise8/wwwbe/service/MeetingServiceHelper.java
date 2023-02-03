package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.ConfirmedPromiseDto;
import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.MeetingUserTimetableEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeetingServiceHelper {
    public static ConfirmedPromiseDto getConfirmedPromise(List<MeetingUserEntity> meetingUserEntityList, Long hostId) {
        ConfirmedPromiseDto confirmedPromiseDto = new ConfirmedPromiseDto();
        for (MeetingUserEntity meetingUser : meetingUserEntityList) {
            if (confirmedPromiseDto.getPromiseDate() == null) {
                for (MeetingUserTimetableEntity meetingUserTimetable : meetingUser.getMeetingUserTimetableEntityList()) {
                    if (meetingUserTimetable.getIsConfirmed()) {
                        confirmedPromiseDto.setPromiseDate(meetingUserTimetable.getPromiseDate());
                        confirmedPromiseDto.setPromiseTime(meetingUserTimetable.getPromiseTime());
                        break;
                    }
                }
            }

            if (confirmedPromiseDto.getPromisePlace() == null) {
                for (MeetingPlaceEntity meetingPlace : meetingUser.getMeetingPlaceEntityList()) {
                    if (meetingPlace.getIsConfirmed()) {
                        confirmedPromiseDto.setPromisePlace(meetingPlace.getPromisePlace());
                        break;
                    }
                }
            }

            if (!meetingUser.getPlaceVoteEntityList().isEmpty()) {
                confirmedPromiseDto.setVotingUserCount(confirmedPromiseDto.getVotingUserCount() + 1);
            }

            if (meetingUser.getMeetingUserId() == hostId) {
                confirmedPromiseDto.setHostName(meetingUser.getMeetingUserName());
            }
        }

        return confirmedPromiseDto;
    }

    public static ConfirmedPromiseDto getHostAndVotingCnt(List<MeetingUserEntity> meetingUserEntityList, Long hostId) {
        ConfirmedPromiseDto confirmedPromiseDto = new ConfirmedPromiseDto();
        for (MeetingUserEntity meetingUser : meetingUserEntityList) {
            if (!meetingUser.getPlaceVoteEntityList().isEmpty()) {
                confirmedPromiseDto.setVotingUserCount(confirmedPromiseDto.getVotingUserCount() + 1);
            }

            if (meetingUser.getMeetingUserId() == hostId) {
                confirmedPromiseDto.setHostName(meetingUser.getMeetingUserName());
            }
        }

        return confirmedPromiseDto;
    }
}
