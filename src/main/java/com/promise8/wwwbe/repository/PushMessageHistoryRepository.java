package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.v1.model.entity.PushMessageHistoryEntityV1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushMessageHistoryRepository extends JpaRepository<PushMessageHistoryEntityV1, Long> {

}
