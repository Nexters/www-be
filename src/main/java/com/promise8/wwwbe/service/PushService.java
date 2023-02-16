package com.promise8.wwwbe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import com.promise8.wwwbe.model.mobile.PushMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PushService {
    private final FirebaseMessaging fcm;
    private final ObjectMapper objectMapper;

    public String send(String token, PushMessage pushMessage) {
        try {
            String jsonStr = objectMapper.writeValueAsString(pushMessage);
            Message message = Message.builder()
                    .setToken(token)
                    .putData("data", jsonStr)
                    .build();
            return fcm.send(message);

        } catch (Exception e) {
            throw new BizException(BaseErrorCode.FCM_SEND_ERROR, e);
        }
    }
}
