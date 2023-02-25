package com.promise8.wwwbe.service;

import com.promise8.wwwbe.model.mobile.PushMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("alpha")
class IntegrationPushServiceTest {

    @Autowired
    PushService pushService;

    @Test
    @Disabled
    void testSend() throws Exception {
        String token = "cORc8O22SDmqneB0GJxlOE:APA91bHHd0pyh0ogt65JDrOZI8HNyc08UXeL93wOTs1PbDeAzz2vTo-md8JevYwZEHsPJ5p3qqt21LooGZ5PMmzh06AVh1hEavTojNwHZ9OTKVu23L_p1QCmHaLd4qSQebeHqsV-2Opy";
        pushService.send(token, new PushMessage(PushMessage.ContentType.MEETING, 2L, "test", "test"));
    }
}