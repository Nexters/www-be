package com.promise8.wwwbe.controller.v1;

import com.promise8.wwwbe.config.security.UserPrincipal;
import com.promise8.wwwbe.service.PushService;
import com.promise8.wwwbe.model.v1.http.BaseResponse;
import com.promise8.wwwbe.model.v1.mobile.PushMessageV1;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pushtest")
@RequiredArgsConstructor
public class V1PushTestController {

    private final PushService pushService;

    @PostMapping("/auth")
    public BaseResponse<Void> pushMessage(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody PushMessageV1 pushMessage
    ) {
        pushService.send(userPrincipal.getFcmToken(), pushMessage);
        return BaseResponse.ok();
    }

    @PostMapping("/withoutAuth")
    public BaseResponse<Void> pushMessage(
            @RequestParam(name = "fcmToken") String fcmToken,
            @RequestBody PushMessageV1 pushMessage
    ) {
        pushService.send(fcmToken, pushMessage);
        return BaseResponse.ok();
    }
}
