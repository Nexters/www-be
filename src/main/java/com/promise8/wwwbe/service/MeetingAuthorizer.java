package com.promise8.wwwbe.service;

import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.UserEntity;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import com.promise8.wwwbe.repository.MeetingRepository;
import com.promise8.wwwbe.repository.MeetingUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingAuthorizer {
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;

    public boolean isCreator(UserPrincipal userPrincipal, long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId).orElseThrow(() -> {
            throw new BizException(BaseErrorCode.INVALID_REQUEST, "not exist meeting");
        });
        UserEntity creator = meetingEntity.getCreator();
        return creator.getUserId() == userPrincipal.getId();
    }

    public boolean isJoinedUser(UserPrincipal userPrincipal, long meetingId) {
        return meetingUserRepository.existsMeetingUserEntityByUserEntity_UserIdAndMeetingEntity_MeetingId(userPrincipal.getId(), meetingId);
    }
}
