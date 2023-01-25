package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingUserTimetableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingUserTimetableRepository extends JpaRepository<MeetingUserTimetableEntity, Long> {
    public List<MeetingUserTimetableEntity> findByMeetingUserEntity_MeetingEntity_MeetingId(long meetingId);
}
