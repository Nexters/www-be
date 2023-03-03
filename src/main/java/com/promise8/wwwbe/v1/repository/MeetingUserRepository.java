package com.promise8.wwwbe.v1.repository;

import com.promise8.wwwbe.v1.model.entity.MeetingEntity;
import com.promise8.wwwbe.v1.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.v1.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUserEntity, Long> {
    Boolean existsMeetingUserEntityByUserEntity_UserIdAndMeetingEntity_MeetingId(Long userId, Long meetingId);

    Optional<MeetingUserEntity> findByMeetingEntityAndUserEntity(MeetingEntity meetingEntity, UserEntity userEntity);

    @Query(value = "select count(distinct pv.meetingUserEntity.meetingUserId) from place_vote pv " +
            "where pv.meetingUserEntity.meetingUserId in " +
            "(select mu.meetingUserId from meeting_user mu where mu.meetingEntity.meetingId = :meetingId)")
    int getVoteFinishedUserCount(long meetingId);
}
