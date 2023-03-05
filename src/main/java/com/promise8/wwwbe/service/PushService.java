package com.promise8.wwwbe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.promise8.wwwbe.repository.PushMessageHistoryRepository;
import com.promise8.wwwbe.v1.model.entity.PushMessageHistoryEntityV1;
import com.promise8.wwwbe.v1.model.exception.BizException;
import com.promise8.wwwbe.v1.model.http.BaseErrorCode;
import com.promise8.wwwbe.v1.model.mobile.PushMessageV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushService {
    private final PushMessageHistoryRepository pushMessageHistoryRepository;
    private final FirebaseMessaging fcm;
    private final ObjectMapper objectMapper;

    public String send(String token, PushMessageV1 pushMessage) {
        try {
            PushMessageHistoryEntityV1 pushMessageHistoryEntity = pushMessageHistoryRepository.save(PushMessageHistoryEntityV1.builder()
                    .title(pushMessage.getTitle())
                    .text(pushMessage.getText())
                    .meetingId(pushMessage.getContentId())
                    .build());

            pushMessage.setId(pushMessageHistoryEntity.getPushMessageHistoryId());
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
