package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.PlaceVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceVoteRepository extends JpaRepository<PlaceVoteEntity, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "delete from place_vote pv where pv.meetingUserEntity = :meetingUserEntity")
    void deleteByMeetingUserEntity(@Param("meetingUserEntity") MeetingUserEntity meetingUserEntity);
}
