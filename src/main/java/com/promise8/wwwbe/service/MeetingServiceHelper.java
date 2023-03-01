package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.PromiseDayOfWeek;
import com.promise8.wwwbe.model.dto.PromiseTime;
import com.promise8.wwwbe.model.dto.UserInfoDto;
import com.promise8.wwwbe.model.dto.res.ConfirmedPromiseResDto;
import com.promise8.wwwbe.model.dto.res.UserPromisePlaceResDto;
import com.promise8.wwwbe.model.dto.res.UserPromiseTimeResDto;
import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.MeetingUserTimetableEntity;
import io.jsonwebtoken.lang.Collections;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
                        userPromiseTime.getUserInfoList().add(
                                new UserInfoDto(
                                        meetingUser.getMeetingUserName(),
                                        ThumbnailHelper.getCharacter(
                                                meetingUser.getUserEntity().getUserId(),
                                                meetingUser.getUserEntity().getUserId() == meetingEntity.getCreator().getUserId()
                                        )
                                )
                        );
                        isAdd = true;
                        break;
                    }
                }

                if (!isAdd) {
                    List<UserInfoDto> userInfoDtoList = new ArrayList<>();
                    userInfoDtoList.add(new UserInfoDto(meetingUser.getMeetingUserName(),
                            ThumbnailHelper.getCharacter(
                                    meetingUser.getUserEntity().getUserId(),
                                    meetingUser.getUserEntity().getUserId() == meetingEntity.getCreator().getUserId()
                            )
                    ));
                    userPromiseTimeResDtoList.add(UserPromiseTimeResDto.builder()
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

    public static List<String> getMeetingUserNameList(MeetingEntity meetingEntity) {
        List<String> userNameList = new ArrayList<>();
        if (Collections.isEmpty(meetingEntity.getMeetingUserEntityList())) {
            return null;
        }

        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            userNameList.add(meetingUser.getMeetingUserName());
        });

        return userNameList;
    }

    public static List<UserInfoDto> getMeetingUserInfoDtoList(MeetingEntity meetingEntity) {
        if (Collections.isEmpty(meetingEntity.getMeetingUserEntityList())) {
            return null;
        }

        List<MeetingUserEntity> meetingUserEntityList = meetingEntity.getMeetingUserEntityList();

        List<UserInfoDto> userInfoDtoList = meetingUserEntityList.stream()
                .map(meetingUserEntity -> {
                    ThumbnailHelper.CharacterType character =
                            ThumbnailHelper.getCharacter(
                                    meetingUserEntity.getUserEntity().getUserId(),
                                    meetingUserEntity.getUserEntity().getUserId() == meetingEntity.getCreator().getUserId()
                            );
                    return new UserInfoDto(meetingUserEntity.getMeetingUserName(), character);
                })
                .collect(Collectors.toList());

        return userInfoDtoList;
    }

    public static List<UserPromisePlaceResDto> getUserPromisePlaceResDtoList(MeetingEntity meetingEntity) {
        List<UserPromisePlaceResDto> userPromisePlaceResDtoList = new ArrayList<>();
        if (meetingEntity.getMeetingUserEntityList() == null || meetingEntity.getMeetingUserEntityList().isEmpty()) {
            return userPromisePlaceResDtoList;
        }

        List<MeetingPlaceEntity> meetingPlaceEntityList = new ArrayList<>();
        meetingEntity.getMeetingUserEntityList().forEach(meetingUser -> {
            meetingPlaceEntityList.addAll(meetingUser.getMeetingPlaceEntityList());
        });
        meetingPlaceEntityList.sort(MeetingServiceHelper::sortingOfPlace);

        userPromisePlaceResDtoList.addAll(meetingPlaceEntityList.stream()
                .map(place -> UserPromisePlaceResDto.of(place, meetingEntity.getCreator().getUserId())).collect(Collectors.toList()));

        return userPromisePlaceResDtoList;
    }

    private static int sortingOfPlace(MeetingPlaceEntity o1, MeetingPlaceEntity o2) {
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
