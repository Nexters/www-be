package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {
    @Query(value = "select m.meeting_code from meeting m where meeting_code= ?1", nativeQuery = true)
    public String isExistMeetingCode(String meetingCode);

    public Optional<MeetingEntity> findByMeetingCode(String meetingCode);

    @Query(value = "select * from meeting m join meeting_user mu join user u on u.device_id = ?1 and mu.user_id = u.user_id and mu.meeting_id = m.meeting_id;", nativeQuery = true)
    public List<MeetingEntity> findByUserEntity_DeviceId(String deviceId);
}
