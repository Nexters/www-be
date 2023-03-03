package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.v1.model.entity.MeetingPlaceEntityV1;
import com.promise8.wwwbe.v1.model.entity.MeetingUserEntityV1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;

public interface MeetingPlaceRepository extends JpaRepository<MeetingPlaceEntityV1, Long> {


    @Query(value = "SELECT m FROM meeting_place m " +
            "WHERE m.meetingPlaceId IN :placeVoteEntityIds")
    List<MeetingPlaceEntityV1> findMeetingPlaceListByPlaceVoteIds(
            @Param("placeVoteEntityIds") List<Long> placeVoteEntityIds);

    List<MeetingPlaceEntityV1> findByMeetingUserEntity(MeetingUserEntityV1 meetingUserEntity);

    @Query(value = "select mp.promisePlace from meeting_place mp " +
            "where mp.meetingUserEntity = :meetingUserEntity")
    public HashSet<String> getExistMeetingPlaceList(MeetingUserEntityV1 meetingUserEntity);

    @Query(value = "select mp.promisePlace " +
            "from meeting_place mp " +
            "where mp.meetingUserEntity.meetingUserId = :meetingUserId")
    String findConfirmedPromiseTime(Long meetingUserId);
}
