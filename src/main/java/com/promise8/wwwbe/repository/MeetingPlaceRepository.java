package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;

public interface MeetingPlaceRepository extends JpaRepository<MeetingPlaceEntity, Long> {


    @Query(value = "SELECT m FROM meeting_place m " +
            "WHERE m.meetingUserEntity = :meetingUserEntity " +
            "AND m.meetingPlaceId IN :placeVoteEntityIds")
    List<MeetingPlaceEntity> findMeetingPlaceListByPlaceVoteIds(
            @Param("meetingUserEntity") MeetingUserEntity meetingUserEntity,
            @Param("placeVoteEntityIds") List<Long> placeVoteEntityIds);
    List<MeetingPlaceEntity> findByMeetingUserEntity(MeetingUserEntity meetingUserEntity);

    @Query(value = "select mp.promisePlace from meeting_place mp " +
            "where mp.meetingUserEntity = :meetingUserEntity")
    public HashSet<String> getExistMeetingPlaceList(MeetingUserEntity meetingUserEntity);
}
