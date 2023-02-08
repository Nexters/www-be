package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUserEntity, Long> {
    Boolean existsMeetingUserEntityByUserEntity_UserIdAndMeetingEntity_MeetingId(Long userId, Long meetingId);
    Optional<MeetingUserEntity> findByMeetingEntityAndUserEntity(MeetingEntity meetingEntity, UserEntity userEntity);
}
