package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.entity.MeetingEntity;
import com.promise8.wwwbe.model.entity.MeetingPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingPlaceRepository extends JpaRepository<MeetingPlaceEntity, Long> {
}
