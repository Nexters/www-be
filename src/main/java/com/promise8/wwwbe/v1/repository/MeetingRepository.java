package com.promise8.wwwbe.v1.repository;

import com.promise8.wwwbe.v1.model.entity.MeetingEntity;
import com.promise8.wwwbe.v1.model.entity.MeetingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {
    @Query(value = "select m.meetingCode from meeting m " +
            "where m.meetingCode = :meetingCode")
    public String isExistMeetingCode(String meetingCode);

    public Optional<MeetingEntity> findByMeetingCode(String meetingCode);

    @Query(value = "select m from meeting_user mu " +
            "join user u on u.userId = mu.userEntity.userId " +
            "join meeting m on m.meetingId = mu.meetingEntity.meetingId " +
            "where u.deviceId = :deviceId")
    public List<MeetingEntity> findByUserEntity_DeviceId(String deviceId);

    @Query(value = "select m from meeting m " +
            "join meeting_user mu on mu.meetingEntity.meetingId = m.meetingId " +
            "join meeting_user_timetable mut on mut.meetingUserEntity.meetingUserId = mu.meetingUserId " +
            "where mut.promiseDate < :nowDate " +
            "and mut.isConfirmed = :isConfirmed " +
            "and m.meetingStatus = :meetingStatus")
    public List<MeetingEntity> findByMeetingStatusAndConfirmedDate(LocalDate nowDate, boolean isConfirmed, MeetingStatus meetingStatus);


    @Query(value = "select m from meeting m " +
            "join meeting_user mu on mu.meetingEntity.meetingId = m.meetingId " +
            "join meeting_user_timetable mut on mut.meetingUserEntity.meetingUserId = mu.meetingUserId " +
            "where mut.promiseDate = :prevDate " +
            "and mut.isConfirmed = :isConfirmed " +
            "and m.meetingStatus = :meetingStatus")
    public List<MeetingEntity> getMeetingOneDayLater(LocalDate prevDate, boolean isConfirmed, MeetingStatus meetingStatus);

    /**
     * VOTED 된 meeting 재촉 알림 용도 쿼리
     * @return
     */
    @Query(value = "select m from meeting m " +
            "where m.voteFinishDateTime <= CURRENT_TIME " +
            "and m.voteFinishDateTime >= :dateTime " +
            "and m.meetingStatus = 'VOTED'")
    public List<MeetingEntity> findVotedMeetingByDateTime(LocalDateTime dateTime);
}
