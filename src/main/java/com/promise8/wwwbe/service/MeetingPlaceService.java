package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.res.UserPromisePlaceResDto;
import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import com.promise8.wwwbe.model.entity.PlaceVoteEntity;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import com.promise8.wwwbe.repository.MeetingPlaceRepository;
import com.promise8.wwwbe.repository.MeetingRepository;
import com.promise8.wwwbe.repository.PlaceVoteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingPlaceService {
    private final MeetingRepository meetingRepository;
    private final PlaceVoteRepository placeVoteRepository;
    MeetingPlaceRepository meetingPlaceRepository;

    public List<UserPromisePlaceResDto> getMeetingPlaceList(Long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        List<UserPromisePlaceResDto> userPromisePlaceResDtoList = MeetingServiceHelper.getUserPromisePlaceResDtoList(meetingEntity);
        List<PlaceVoteEntity> placeVoteEntityList = placeVoteRepository.getPlaceVoteListByMeetingId(meetingId);

        List<Long> placeVoteIds = placeVoteEntityList.stream().map(PlaceVoteEntity::getPlaceVoteId).collect(Collectors.toList());
        List<MeetingPlaceEntity> meetingPlaceListByPlaceVoteIds = meetingPlaceRepository.findMeetingPlaceListByPlaceVoteIds(placeVoteIds);

        List<Pair<Long, Integer>> placeIdAndCountList = meetingPlaceListByPlaceVoteIds.stream()
                .map(meetingPlaceEntity -> Pair.of(meetingPlaceEntity.getMeetingPlaceId(), meetingPlaceEntity.getPlaceVoteEntityList().size())).collect(Collectors.toList());

        for (UserPromisePlaceResDto userPromisePlaceResDto : userPromisePlaceResDtoList) {
            for (Pair<Long, Integer> placeIdAndCount : placeIdAndCountList) {
                Long placeId = placeIdAndCount.getLeft();
                if (userPromisePlaceResDto.getPlaceId() == placeId) {
                    Integer votedUserCount = placeIdAndCount.getRight();
                    userPromisePlaceResDto.setVotedUserCount(votedUserCount);
                }
            }
        }
        return userPromisePlaceResDtoList;
    }
}
