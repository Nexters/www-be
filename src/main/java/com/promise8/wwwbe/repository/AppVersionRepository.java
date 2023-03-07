package com.promise8.wwwbe.repository;

import com.promise8.wwwbe.model.v1.dto.PlatformTypeV1;
import com.promise8.wwwbe.model.v1.entity.AppVersionEntityV1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVersionRepository extends JpaRepository<AppVersionEntityV1, Long> {
    AppVersionEntityV1 findTop1ByPlatformType(PlatformTypeV1 platformType);
}
