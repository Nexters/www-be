package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingPlaceRepository extends JpaRepository<MeetingPlaceEntity, Long> {
    public List<MeetingPlaceEntity> findByMeetingUserEntity_MeetingEntity_MeetingId(long meetingId);
}
