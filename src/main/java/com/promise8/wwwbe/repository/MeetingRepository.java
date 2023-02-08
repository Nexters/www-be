package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {
    @Query(value = "select m.meeting_code from meeting m where meeting_code= ?1", nativeQuery = true)
    public String isExistMeetingCode(String meetingCode);

    public Optional<MeetingEntity> findByMeetingCode(String meetingCode);

    @Query(value = "select * from meeting m join meeting_user mu join user u on u.device_id = ?1 and mu.user_id = u.user_id and mu.meeting_id = m.meeting_id", nativeQuery = true)
    public List<MeetingEntity> findByUserEntity_DeviceId(String deviceId);

    @Query(value = "select * from meeting m join meeting_user mu join meeting_user_timetable mut on mut.promise_date < ?1 and mut.is_confirmed = ?2 and mut.meeting_user_id = mu.meeting_user_id and mu.meeting_id = m.meeting_id and m.meeting_status = ?3", nativeQuery = true)
    public List<MeetingEntity> findByMeetingStatusAndConfirmedDate(LocalDate nowDate, boolean isConfirmed, String meetingStatus);
}
