package com.promise8.wwwbe.v1.service;

import com.promise8.wwwbe.v1.model.dto.res.UserPromisePlaceListResDto;
import com.promise8.wwwbe.v1.model.dto.res.UserPromisePlaceResDto;
import com.promise8.wwwbe.v1.model.entity.MeetingEntity;
import com.promise8.wwwbe.v1.model.exception.BizException;
import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
import com.promise8.wwwbe.v1.repository.MeetingRepository;
import com.promise8.wwwbe.v1.repository.MeetingUserRepository;
import com.promise8.wwwbe.v1.repository.PlaceVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingPlaceService {
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final PlaceVoteRepository placeVoteRepository;

    public UserPromisePlaceListResDto getMeetingPlaceList(Long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        List<UserPromisePlaceResDto> userPromisePlaceResDtoList = MeetingServiceHelper.getUserPromisePlaceResDtoList(meetingEntity);

        int voteFinishedUserCount = meetingUserRepository.getVoteFinishedUserCount(meetingId);
        return new UserPromisePlaceListResDto(userPromisePlaceResDtoList, voteFinishedUserCount);
    }
}
