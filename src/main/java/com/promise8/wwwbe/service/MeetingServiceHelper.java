package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.PromiseDayOfWeek;
import com.promise8.wwwbe.model.dto.PromiseTime;
import com.promise8.wwwbe.model.dto.res.ConfirmedPromiseResDto;
import com.promise8.wwwbe.model.dto.res.UserPromiseTimeResDto;
import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.MeetingUserTimetableEntity;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static List<UserPromiseTimeResDto> getUserPromiseTimeList(MeetingEntity meetingEntity) {
        List<UserPromiseTimeResDto> userPromiseTimeResDtoList = new ArrayList<>();
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            return userPromiseTimeResDtoList;
        }

        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            meetingUser.getMeetingUserTimetableEntityList().forEach(res -> {
                LocalDate promiseDate = res.getPromiseDate();
                PromiseTime promiseTime = res.getPromiseTime();

                boolean isAdd = false;
                for (UserPromiseTimeResDto userPromiseTime : userPromiseTimeResDtoList) {
                    if (userPromiseTime.getPromiseDate().equals(promiseDate) && userPromiseTime.getPromiseTime().equals(promiseTime)) {
                        userPromiseTime.getUserNameList().add(meetingUser.getMeetingUserName());
                        isAdd = true;
                        break;
                    }
                }

                if (!isAdd) {
                    List<String> userNameList = new ArrayList<>();
                    userNameList.add(meetingUser.getMeetingUserName());
                    userPromiseTimeResDtoList.add(UserPromiseTimeResDto.builder()
                            .promiseDate(promiseDate)
                            .promiseTime(promiseTime)
                            .promiseDayOfWeek(getPromiseDayOfWeek(promiseDate))
                            .userNameList(userNameList)
                            .build());
                }
            });
        });

        userPromiseTimeResDtoList.sort((o1, o2) -> o2.getUserNameList().size() - o1.getUserNameList().size());
        return userPromiseTimeResDtoList;
    }

    private static PromiseDayOfWeek getPromiseDayOfWeek(LocalDate promiseDate) {
        DayOfWeek dayOfWeek = promiseDate.getDayOfWeek();
        int idx = dayOfWeek.getValue();
        return PromiseDayOfWeek.values()[idx - 1];
    }

    public static HashMap<String, List<String>> getUserVoteHashMap(MeetingEntity meetingEntity) {
        HashMap<String, List<String>> userVoteHashMap = new HashMap<>();
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            return userVoteHashMap;
        }

        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            meetingUser.getMeetingPlaceEntityList().forEach(meetingPlace -> {
                meetingPlace.getPlaceVoteEntityList().forEach(res -> {
                    String promisePlace = res.getMeetingPlaceEntity().getPromisePlace();
                    if (userVoteHashMap.containsKey(promisePlace)) {
                        userVoteHashMap.get(promisePlace).add(res.getMeetingUserEntity().getMeetingUserName());
                    } else {
                        List<String> userNameList = new ArrayList<>();
                        userNameList.add(res.getMeetingUserEntity().getMeetingUserName());
                        userVoteHashMap.put(promisePlace, userNameList);
                    }
                });
            });
        });

        return userVoteHashMap;
    }
}
