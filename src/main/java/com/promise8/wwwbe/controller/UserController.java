package com.promise8.wwwbe.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/join")
    public String join(@RequestBody String deviceId) {
        return "join";
    }
}
