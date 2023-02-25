package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import com.promise8.wwwbe.model.entity.MeetingUserTimetableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingUserTimetableRepository extends JpaRepository<MeetingUserTimetableEntity, Long> {
    List<MeetingUserTimetableEntity> findByMeetingUserEntity(MeetingUserEntity meetingUserEntity);

    @Query(value = "select mut.promiseTime " +
            "from meeting_user_timetable mut " +
            "where mut.meetingUserEntity.meetingUserId = :meetingUserId")
    String findConfirmedPromiseTime(Long meetingUserId);
}
