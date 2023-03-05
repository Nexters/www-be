package com.promise8.wwwbe.service;

import com.promise8.wwwbe.repository.MeetingRepository;
import com.promise8.wwwbe.repository.MeetingUserRepository;
import com.promise8.wwwbe.repository.PlaceVoteRepository;
import com.promise8.wwwbe.model.v1.dto.res.UserPromisePlaceListResDtoV1;
import com.promise8.wwwbe.model.v1.dto.res.UserPromisePlaceResDtoV1;
import com.promise8.wwwbe.model.v1.entity.MeetingEntityV1;
import com.promise8.wwwbe.model.v1.exception.BizException;
import com.promise8.wwwbe.model.v1.http.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingPlaceService {
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final PlaceVoteRepository placeVoteRepository;

    public UserPromisePlaceListResDtoV1 getMeetingPlaceList(Long meetingId) {
        MeetingEntityV1 meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        List<UserPromisePlaceResDtoV1> userPromisePlaceResDtoList = MeetingServiceHelper.getUserPromisePlaceResDtoList(meetingEntity);

        int voteFinishedUserCount = meetingUserRepository.getVoteFinishedUserCount(meetingId);
        return new UserPromisePlaceListResDtoV1(userPromisePlaceResDtoList, voteFinishedUserCount);
    }
}
