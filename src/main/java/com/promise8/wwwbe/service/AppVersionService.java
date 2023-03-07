package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.v1.dto.PlatformTypeV1;
import com.promise8.wwwbe.model.v1.entity.AppVersionEntityV1;
import com.promise8.wwwbe.repository.AppVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppVersionService {
    private final AppVersionRepository appVersionRepository;

    public String getCurrentAppVersion(PlatformTypeV1 platformType) {
        AppVersionEntityV1 appVersion = appVersionRepository.findTop1ByPlatformType(platformType);
        return appVersion.getVersion();
    }
}
