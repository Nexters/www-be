package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.dto.PlatformType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("alpha")
class LinkServiceTest {

    @Autowired
    LinkService linkService;

    @Test
    void createLink() {
        linkService.createLink(PlatformType.IOS, "https://naver.com");
    }
}