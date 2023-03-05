package com.promise8.wwwbe.service;

import com.promise8.wwwbe.repository.MeetingRepository;
import com.promise8.wwwbe.model.v1.dto.res.UserPromiseTimeResDtoV1;
import com.promise8.wwwbe.model.v1.entity.MeetingEntityV1;
import com.promise8.wwwbe.model.v1.exception.BizException;
import com.promise8.wwwbe.model.v1.http.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingUserTimetableService {
    private final MeetingRepository meetingRepository;

    public List<UserPromiseTimeResDtoV1> getUserPromiseTimeList(Long meetingId) {
        MeetingEntityV1 meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        return MeetingServiceHelper.getUserPromiseTimeList(meetingEntity);
    }
}
