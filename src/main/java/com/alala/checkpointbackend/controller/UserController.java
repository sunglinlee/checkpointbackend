package com.alala.checkpointbackend.controller;

import com.alala.checkpointbackend.model.MailLoginRequest;
import com.alala.checkpointbackend.model.MailRegisterRequest;
import com.alala.checkpointbackend.model.UserLoginRequest;
import com.alala.checkpointbackend.model.UserRegisterRequest;
import com.alala.checkpointbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user", description = "會員相關API")
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(description = "一般註冊")
    @PostMapping(value = "/register")
    public String register(@RequestBody UserRegisterRequest request) {
        return userService.register(request);
    }

    @Operation(description = "信箱註冊")
    @PostMapping(value = "/mailRegister")
    public String mailRegister(@RequestBody MailRegisterRequest request) {
        return userService.mailRegister(request);
    }

    @Operation(description = "信箱登入")
    @PostMapping(value = "/mailLogin")
    public String mailLogin(@RequestBody MailLoginRequest request) {
        return userService.mailLogin(request);
    }

    @Operation(description = "一般登入")
    @PostMapping(value = "/login")
    public String register(@RequestBody UserLoginRequest request) {
        return userService.login(request);
    }

    @Operation(description = "登出")
    @PostMapping(value = "/logout")
    public String logout(@RequestBody UserLoginRequest request) {
        return userService.logout(request);
    }

    @Operation(description = "刷新token")
    @GetMapping(value = "/refreshToken")
    public String refreshToken(@RequestParam String email) {
        return userService.refreshToken(email);
    }
}
