package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUserEntity, Long> {
    public List<MeetingUserEntity> findByMeetingEntity_MeetingId(long meetingId);

    public Optional<MeetingUserEntity> findByUserEntity_UserIdAndMeetingEntity_MeetingId(long userId, long meetingId);
}
