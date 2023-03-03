package com.promise8.wwwbe.v1.service;

import com.promise8.wwwbe.v1.config.security.UserPrincipal;
import com.promise8.wwwbe.v1.model.entity.MeetingEntity;
import com.promise8.wwwbe.v1.model.entity.MeetingStatus;
import com.promise8.wwwbe.v1.model.entity.UserEntity;
import com.promise8.wwwbe.v1.model.exception.BizException;
import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
import com.promise8.wwwbe.v1.repository.MeetingRepository;
import com.promise8.wwwbe.v1.repository.MeetingUserRepository;
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
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.NOT_EXIST_MEETING);
        });
        UserEntity creator = meetingEntity.getCreator();
        return Objects.equals(creator.getUserId(), userPrincipal.getId());
    }

    public boolean isMeetingVoting(long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.NOT_EXIST_MEETING);
        });

        if (!MeetingStatus.VOTING.equals(meetingEntity.getMeetingStatus())) {
            throw new BizException(BaseErrorCode.NOT_MEETING_STATUS_VOTING);
        }

        return true;
    }
}
