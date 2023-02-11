package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.MeetingUserTimetableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingUserTimetableRepository extends JpaRepository<MeetingUserTimetableEntity, Long> {
    List<MeetingUserTimetableEntity> findByMeetingUserEntity(MeetingUserEntity meetingUserEntity);
}
