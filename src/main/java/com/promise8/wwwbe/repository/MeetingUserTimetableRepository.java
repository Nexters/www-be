package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingUserTimetableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingUserTimetableRepository extends JpaRepository<MeetingUserTimetableEntity, Long> {
    
}
