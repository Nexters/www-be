package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.v1.entity.PushMessageHistoryEntityV1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushMessageHistoryRepository extends JpaRepository<PushMessageHistoryEntityV1, Long> {

}
