package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.model.http.BaseResponse;
import com.promise8.wwwbe.model.mobile.PushMessage;
import com.promise8.wwwbe.service.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pushtest")
@RequiredArgsConstructor
public class PushTestController {

    private final PushService pushService;

    @PostMapping("/auth")
    public BaseResponse<Void> pushMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody PushMessage pushMessage
    ) {
        pushService.send(userPrincipal.getFcmToken(), pushMessage);
        return BaseResponse.ok();
    }

    @PostMapping("/withoutAuth")
    public BaseResponse<Void> pushMessage(
            @RequestParam(name = "fcmToken") String fcmToken,
            @RequestBody PushMessage pushMessage
    ) {
        pushService.send(fcmToken, pushMessage);
        return BaseResponse.ok();
    }
}
