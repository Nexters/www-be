package com.promise8.wwwbe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.promise8.wwwbe.model.mobile.PushMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class PushServiceTest {

    @InjectMocks
    PushService pushService;

    @Mock
    FirebaseMessaging fcm;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        pushService = new PushService(fcm, objectMapper);
    }

    @Test
    void testSend() throws Exception {
        // given
        when(fcm.send(any(Message.class))).thenReturn("1");

        // when
        String id = pushService.send("token", new PushMessage(PushMessage.ContentType.MEETING, 1L, "testBody"));

        // then
        Assertions.assertEquals("1", id);
        verify(fcm, times(1)).send(any());
    }

}