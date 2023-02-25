package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.res.UserPromiseTimeResDto;
import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import com.promise8.wwwbe.repository.MeetingRepository;
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
