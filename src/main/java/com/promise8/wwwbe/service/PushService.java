package com.promise8.wwwbe.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.promise8.wwwbe.model.exception.BizException;
import com.promise8.wwwbe.model.http.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushService {
    private final FirebaseMessaging fcm;

    /**
     * TODO: user join시에 token 정보 받아야 할 듯(deviceId와 다른 값으로 보임)
     * TODO: 통합 테스트 필요
     *
     * @param token device 별로 고유한 토큰 (즉, 유저)
     * @param body  보낼 메세지 body
     * @return
     */
    public String send(String token, String body) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .putData("body", body)
                    .build();
            return fcm.send(message);

        } catch (Exception e) {
            throw new BizException(BaseErrorCode.FCM_SEND_ERROR, e);
        }
    }
}
