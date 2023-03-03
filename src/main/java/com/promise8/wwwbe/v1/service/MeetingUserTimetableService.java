package com.promise8.wwwbe.v1.service;

import com.promise8.wwwbe.v1.model.dto.res.UserPromiseTimeResDto;
import com.promise8.wwwbe.v1.model.entity.MeetingEntity;
import com.promise8.wwwbe.v1.model.exception.BizException;
import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
import com.promise8.wwwbe.v1.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingUserTimetableService {
    private final MeetingRepository meetingRepository;

    public List<UserPromiseTimeResDto> getUserPromiseTimeList(Long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BizException(BaseErrorCode.NOT_EXIST_MEETING));
        return MeetingServiceHelper.getUserPromiseTimeList(meetingEntity);
    }
}
