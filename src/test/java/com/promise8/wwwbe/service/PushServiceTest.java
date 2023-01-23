package com.promise8.wwwbe.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class PushServiceTest {

    @InjectMocks
    PushService pushService;

    @Mock
    FirebaseMessaging fcm;

    @Test
    void testSend() throws Exception {
        // given
        when(fcm.send(any(Message.class))).thenReturn("1");

        // when
        String id = pushService.send("token", "testBody");

        // then
        Assertions.assertEquals("1", id);
        verify(fcm, times(1)).send(any());
    }

}