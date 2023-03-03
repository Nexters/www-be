package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.v1.model.entity.MeetingEntityV1;
import com.promise8.wwwbe.v1.model.entity.MeetingUserEntityV1;
import com.promise8.wwwbe.v1.model.entity.UserEntityV1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUserEntityV1, Long> {
    Boolean existsMeetingUserEntityByUserEntity_UserIdAndMeetingEntity_MeetingId(Long userId, Long meetingId);

    Optional<MeetingUserEntityV1> findByMeetingEntityAndUserEntity(MeetingEntityV1 meetingEntity, UserEntityV1 userEntity);

    @Query(value = "select count(distinct pv.meetingUserEntity.meetingUserId) from place_vote pv " +
            "where pv.meetingUserEntity.meetingUserId in " +
            "(select mu.meetingUserId from meeting_user mu where mu.meetingEntity.meetingId = :meetingId)")
    int getVoteFinishedUserCount(long meetingId);
}
