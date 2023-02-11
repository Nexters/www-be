package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {
    @Query(value = "select m.meetingCode from meeting m " +
            "where m.meetingCode = :meetingCode")
    public String isExistMeetingCode(String meetingCode);

    public Optional<MeetingEntity> findByMeetingCode(String meetingCode);

    @Query(value = "select m from meeting m " +
            "join meeting_user mu " +
            "join user u " +
            "on u.deviceId = :deviceId " +
            "and mu.userEntity.userId = u.userId " +
            "and mu.meetingEntity.meetingId = m.meetingId")
    public List<MeetingEntity> findByUserEntity_DeviceId(String deviceId);

    @Query(value = "select m from meeting m " +
            "join meeting_user mu " +
            "join meeting_user_timetable mut " +
            "on mut.promiseDate < :nowDate " +
            "and mut.isConfirmed = :isConfirmed " +
            "and mut.meetingUserEntity.meetingUserId = mu.meetingUserId " +
            "and mu.meetingEntity.meetingId = m.meetingId " +
            "and m.meetingStatus = :meetingStatus")
    public List<MeetingEntity> findByMeetingStatusAndConfirmedDate(LocalDate nowDate, boolean isConfirmed, String meetingStatus);
}
