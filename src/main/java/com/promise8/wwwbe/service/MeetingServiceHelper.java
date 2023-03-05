package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.v1.dto.PromiseDayOfWeek;
import com.promise8.wwwbe.model.v1.dto.PromiseTime;
import com.promise8.wwwbe.model.v1.dto.res.ConfirmedPromiseResDtoV1;
import com.promise8.wwwbe.model.v1.dto.res.UserInfoDtoV1;
import com.promise8.wwwbe.model.v1.dto.res.UserPromisePlaceResDtoV1;
import com.promise8.wwwbe.model.v1.dto.res.UserPromiseTimeResDtoV1;
import com.promise8.wwwbe.model.v1.entity.MeetingEntityV1;
import com.promise8.wwwbe.model.v1.entity.MeetingPlaceEntityV1;
import com.promise8.wwwbe.model.v1.entity.MeetingUserEntityV1;
import com.promise8.wwwbe.model.v1.entity.MeetingUserTimetableEntityV1;
import io.jsonwebtoken.lang.Collections;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MeetingServiceHelper {
    public static ConfirmedPromiseResDtoV1 getConfirmedPromise(List<MeetingUserEntityV1> meetingUserEntityList, Long hostId) {
        ConfirmedPromiseResDtoV1 confirmedPromiseResDto = new ConfirmedPromiseResDtoV1();
        if (meetingUserEntityList == null || meetingUserEntityList.isEmpty()) return confirmedPromiseResDto;

        for (MeetingUserEntityV1 meetingUser : meetingUserEntityList) {
            if (confirmedPromiseResDto.getPromiseDate() == null) {
                for (MeetingUserTimetableEntityV1 meetingUserTimetable : meetingUser.getMeetingUserTimetableEntityList()) {
                    if (meetingUserTimetable.getIsConfirmed()) {
                        confirmedPromiseResDto.setPromiseDate(meetingUserTimetable.getPromiseDate());
                        confirmedPromiseResDto.setPromiseTime(meetingUserTimetable.getPromiseTime());
                        break;
                    }
                }
            }

            if (confirmedPromiseResDto.getPromisePlace() == null) {
                for (MeetingPlaceEntityV1 meetingPlace : meetingUser.getMeetingPlaceEntityList()) {
                    if (meetingPlace.getIsConfirmed()) {
                        confirmedPromiseResDto.setPromisePlace(meetingPlace.getPromisePlace());
                        break;
                    }
                }
            }

            if (!meetingUser.getPlaceVoteEntityList().isEmpty()) {
                confirmedPromiseResDto.setVotingUserCount(confirmedPromiseResDto.getVotingUserCount() + 1);
            }

            if (Objects.equals(meetingUser.getUserEntity().getUserId(), hostId)) {
                confirmedPromiseResDto.setHostName(meetingUser.getMeetingUserName());
            }
        }

        return confirmedPromiseResDto;
    }

    public static ConfirmedPromiseResDtoV1 getHostAndVotingCnt(List<MeetingUserEntityV1> meetingUserEntityList, Long hostId) {
        ConfirmedPromiseResDtoV1 confirmedPromiseResDto = new ConfirmedPromiseResDtoV1();
        if (meetingUserEntityList == null || meetingUserEntityList.isEmpty()) return confirmedPromiseResDto;

        for (MeetingUserEntityV1 meetingUser : meetingUserEntityList) {
            if (!meetingUser.getPlaceVoteEntityList().isEmpty()) {
                confirmedPromiseResDto.setVotingUserCount(confirmedPromiseResDto.getVotingUserCount() + 1);
            }

            if (Objects.equals(meetingUser.getUserEntity().getUserId(), hostId)) {
                confirmedPromiseResDto.setHostName(meetingUser.getMeetingUserName());
            }
        }

        return confirmedPromiseResDto;
    }

    public static List<UserPromiseTimeResDtoV1> getUserPromiseTimeList(MeetingEntityV1 meetingEntity) {
        List<UserPromiseTimeResDtoV1> userPromiseTimeResDtoList = new ArrayList<>();
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            return userPromiseTimeResDtoList;
        }

        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            meetingUser.getMeetingUserTimetableEntityList().forEach(res -> {
                LocalDate promiseDate = res.getPromiseDate();
                PromiseTime promiseTime = res.getPromiseTime();

                boolean isAdd = false;
                for (UserPromiseTimeResDtoV1 userPromiseTime : userPromiseTimeResDtoList) {
                    if (userPromiseTime.getPromiseDate().equals(promiseDate) && userPromiseTime.getPromiseTime().equals(promiseTime)) {
                        userPromiseTime.getUserInfoList().add(
                                new UserInfoDtoV1(
                                        meetingUser.getMeetingUserName(),
                                        ThumbnailHelper.getCharacter(
                                                meetingUser.getUserEntity().getUserId(),
                                                meetingUser.getUserEntity().getUserId().equals(meetingEntity.getCreator().getUserId())
                                        )
                                )
                        );
                        isAdd = true;
                        break;
                    }
                }

                if (!isAdd) {
                    List<UserInfoDtoV1> userInfoDtoList = new ArrayList<>();
                    userInfoDtoList.add(new UserInfoDtoV1(meetingUser.getMeetingUserName(),
                            ThumbnailHelper.getCharacter(
                                    meetingUser.getUserEntity().getUserId(),
                                    meetingUser.getUserEntity().getUserId().equals(meetingEntity.getCreator().getUserId())
                            )
                    ));
                    userPromiseTimeResDtoList.add(UserPromiseTimeResDtoV1.builder()
                            .timetableId(res.getMeetingUserTimetableId())
                            .promiseDate(promiseDate)
                            .promiseTime(promiseTime)
                            .promiseDayOfWeek(getPromiseDayOfWeek(promiseDate))
                            .userInfoList(userInfoDtoList)
                            .build());
                }
            });
        });

        userPromiseTimeResDtoList.sort((o1, o2) -> o2.getUserInfoList().size() - o1.getUserInfoList().size());
        return userPromiseTimeResDtoList;
    }

    public static PromiseDayOfWeek getPromiseDayOfWeek(LocalDate promiseDate) {
        if (promiseDate == null) {
            return null;
        }

        DayOfWeek dayOfWeek = promiseDate.getDayOfWeek();
        int idx = dayOfWeek.getValue();
        return PromiseDayOfWeek.values()[idx - 1];
    }

    public static HashMap<String, List<String>> getUserVoteHashMap(MeetingEntityV1 meetingEntity) {
        HashMap<String, List<String>> userVoteHashMap = new HashMap<>();
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            return userVoteHashMap;
        }

        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            meetingUser.getMeetingPlaceEntityList().forEach(meetingPlace -> {
                meetingPlace.getPlaceVoteEntityList().forEach(res -> {
                    String promisePlace = res.getMeetingPlaceEntity().getPromisePlace();
                    if (userVoteHashMap.containsKey(promisePlace)) {
                        List<String> userNameList = userVoteHashMap.get(promisePlace);
                        userNameList.add(res.getMeetingUserEntity().getMeetingUserName());
                        userVoteHashMap.put(promisePlace, userNameList);
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

    public static List<String> getMeetingUserNameList(MeetingEntityV1 meetingEntity) {
        List<String> userNameList = new ArrayList<>();
        if (Collections.isEmpty(meetingEntity.getMeetingUserEntityList())) {
            return null;
        }

        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            userNameList.add(meetingUser.getMeetingUserName());
        });

        return userNameList;
    }

    public static List<UserInfoDtoV1> getMeetingUserInfoDtoList(MeetingEntityV1 meetingEntity) {
        if (Collections.isEmpty(meetingEntity.getMeetingUserEntityList())) {
            return null;
        }

        List<MeetingUserEntityV1> meetingUserEntityList = meetingEntity.getMeetingUserEntityList();

        List<UserInfoDtoV1> userInfoDtoList = meetingUserEntityList.stream()
                .map(meetingUserEntity -> {
                    ThumbnailHelper.CharacterType character =
                            ThumbnailHelper.getCharacter(
                                    meetingUserEntity.getUserEntity().getUserId(),
                                    meetingUserEntity.getUserEntity().getUserId().equals(meetingEntity.getCreator().getUserId())
                            );
                    return new UserInfoDtoV1(meetingUserEntity.getMeetingUserName(), character);
                })
                .collect(Collectors.toList());

        return userInfoDtoList;
    }

    public static List<UserPromisePlaceResDtoV1> getUserPromisePlaceResDtoList(MeetingEntityV1 meetingEntity) {
        List<UserPromisePlaceResDtoV1> userPromisePlaceResDtoList = new ArrayList<>();
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            return userPromisePlaceResDtoList;
        }

        List<MeetingPlaceEntityV1> meetingPlaceEntityList = new ArrayList<>();
        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            meetingPlaceEntityList.addAll(meetingUser.getMeetingPlaceEntityList());
        });
        meetingPlaceEntityList.sort(MeetingServiceHelper::sortingOfPlace);

        userPromisePlaceResDtoList.addAll(meetingPlaceEntityList.stream()
                .map(place -> UserPromisePlaceResDtoV1.of(place, meetingEntity.getCreator().getUserId())).collect(Collectors.toList()));

        return userPromisePlaceResDtoList;
    }

    private static int sortingOfPlace(MeetingPlaceEntityV1 o1, MeetingPlaceEntityV1 o2) {
        if (o1.getPlaceVoteEntityList() == null && o2.getPlaceVoteEntityList() == null) {
            return 0;
        } else if (o1.getPlaceVoteEntityList() == null) {
            return 1;
        } else if (o2.getPlaceVoteEntityList() == null) {
            return -1;
        } else {
            return o2.getPlaceVoteEntityList().size() - o1.getPlaceVoteEntityList().size();
        }
    }
}
