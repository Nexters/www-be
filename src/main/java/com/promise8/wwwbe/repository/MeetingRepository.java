package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {
    @Query(value = "select m.meeting_code from meeting m where meeting_code= ?1", nativeQuery = true)
    public String findByMeetingCode(String meetingCode);
}
