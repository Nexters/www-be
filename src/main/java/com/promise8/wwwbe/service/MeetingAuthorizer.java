package com.promise8.wwwbe.service;

import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.repository.MeetingRepository;
import com.promise8.wwwbe.repository.MeetingUserRepository;
import com.promise8.wwwbe.v1.model.entity.MeetingEntityV1;
import com.promise8.wwwbe.v1.model.entity.MeetingStatusV1;
import com.promise8.wwwbe.v1.model.entity.UserEntityV1;
import com.promise8.wwwbe.v1.model.exception.BizException;
import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingAuthorizer {
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;

    public boolean isCreator(UserPrincipal userPrincipal, long meetingId) {
        MeetingEntityV1 meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.NOT_EXIST_MEETING);
        });
        UserEntityV1 creator = meetingEntity.getCreator();
        return Objects.equals(creator.getUserId(), userPrincipal.getId());
    }

    public boolean isMeetingVoting(long meetingId) {
        MeetingEntityV1 meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.NOT_EXIST_MEETING);
        });

        if (!MeetingStatusV1.VOTING.equals(meetingEntity.getMeetingStatus())) {
            throw new BizException(BaseErrorCode.NOT_MEETING_STATUS_VOTING);
        }

        return true;
    }
}
