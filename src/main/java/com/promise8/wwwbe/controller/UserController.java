package com.promise8.wwwbe.controller;

import com.promise8.wwwbe.model.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public BaseResponse<String> join(@RequestBody String deviceId) {

        // TODO: advice 테스트용. 추후 변경 필요
        throw new BizException();
    }
}
