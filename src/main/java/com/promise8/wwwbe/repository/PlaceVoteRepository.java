package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.PlaceVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceVoteRepository extends JpaRepository<PlaceVoteEntity, Long> {
    public List<PlaceVoteEntity> findByMeetingPlaceEntity_MeetingUserEntity_MeetingEntity_MeetingId(long meetingId);
}
