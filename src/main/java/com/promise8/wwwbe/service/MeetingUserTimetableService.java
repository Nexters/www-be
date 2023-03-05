package com.promise8.wwwbe.service;

import com.promise8.wwwbe.repository.MeetingRepository;
import com.promise8.wwwbe.v1.model.dto.res.UserPromiseTimeResDtoV1;
import com.promise8.wwwbe.v1.model.entity.MeetingEntityV1;
import com.promise8.wwwbe.v1.model.exception.BizException;
import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
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
