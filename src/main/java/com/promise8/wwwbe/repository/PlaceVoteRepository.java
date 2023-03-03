package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.v1.model.entity.MeetingUserEntityV1;
import com.promise8.wwwbe.v1.model.entity.PlaceVoteEntityV1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.util.List;

public interface PlaceVoteRepository extends JpaRepository<PlaceVoteEntityV1, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "delete from place_vote pv where pv.meetingUserEntity = :meetingUserEntity")
    void deleteByMeetingUserEntity(@Param("meetingUserEntity") MeetingUserEntityV1 meetingUserEntity);

    @Query(value = "select count(distinct pv.meetingUserEntity.meetingUserId) " +
            "from meeting_user mu " +
            "join fetch place_vote pv " +
            "on mu.meetingUserId = pv.meetingUserEntity.meetingUserId " +
            "where mu.meetingEntity.meetingId = :meetingId")
    int getVotedUserCount(@Param("meetingId") long meetingId);

    @Query("SELECT pv.meetingPlaceEntity.meetingPlaceId as placeId, COUNT(pv.placeVoteId) as count " +
            "FROM place_vote pv " +
            "WHERE pv.meetingPlaceEntity IN " +
            "(SELECT mp FROM meeting_place mp WHERE mp.meetingUserEntity.meetingEntity.meetingId = :meetingId) " +
            "GROUP BY pv.meetingPlaceEntity.meetingPlaceId")
    List<Tuple> getResult(Long meetingId);


}