package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.v1.entity.MeetingUserEntityV1;
import com.promise8.wwwbe.model.v1.entity.MeetingUserTimetableEntityV1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingUserTimetableRepository extends JpaRepository<MeetingUserTimetableEntityV1, Long> {
    List<MeetingUserTimetableEntityV1> findByMeetingUserEntity(MeetingUserEntityV1 meetingUserEntity);

    @Query(value = "select mut.promiseTime " +
            "from meeting_user_timetable mut " +
            "where mut.meetingUserEntity.meetingUserId = :meetingUserId")
    String findConfirmedPromiseTime(Long meetingUserId);
}
