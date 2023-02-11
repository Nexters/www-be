package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingUserRepository extends JpaRepository<MeetingUserEntity, Long> {
    Boolean existsMeetingUserEntityByUserEntity_UserIdAndMeetingEntity_MeetingId(Long userId, Long meetingId);
}
