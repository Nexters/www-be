package com.promise8.wwwbe.v1.repository;

import com.promise8.wwwbe.v1.model.entity.PushMessageHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushMessageHistoryRepository extends JpaRepository<PushMessageHistoryEntity, Long> {

}
