package com.promise8.wwwbe.service;

import com.promise8.wwwbe.v1.model.dto.res.DynamicLinkResDtoV1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("alpha")
class LinkServiceTest {

    @Autowired
    LinkService linkService;

    @Test
    void createLink() {
        DynamicLinkResDtoV1 dynamicLink = linkService.createLink("abcde");
        System.out.println(dynamicLink);
    }
}