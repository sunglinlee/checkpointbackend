package com.alala.checkpointbackend.controller;

import com.alala.checkpointbackend.enums.StatusCode;
import com.alala.checkpointbackend.exception.DuplicateUserException;
import com.alala.checkpointbackend.exception.UserNotLoginException;
import com.alala.checkpointbackend.exception.WrongPasswordException;
import com.alala.checkpointbackend.model.*;
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
    public BaseResponse<User> register(@RequestBody UserRegisterRequest request) throws DuplicateUserException {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), userService.register(request));
    }

    @Operation(description = "信箱登入")
    @PostMapping(value = "/mailLogin")
    public BaseResponse<User> mailLogin(@RequestBody MailLoginRequest request) {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), userService.mailLogin(request));
    }

    @Operation(description = "一般登入")
    @PostMapping(value = "/login")
    public BaseResponse<User> register(@RequestBody UserLoginRequest request) throws WrongPasswordException {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), userService.login(request));
    }

    @Operation(description = "登出")
    @PostMapping(value = "/logout")
    public BaseResponse<String> logout(@RequestBody UserLoginRequest request) throws UserNotLoginException {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), userService.logout(request));
    }

    @Operation(description = "刷新token")
    @GetMapping(value = "/refreshToken")
    public BaseResponse<String> refreshToken(@RequestParam String email) {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), userService.refreshToken(email));
    }

    @Operation(description = "修改暱稱")
    @PostMapping(value = "/change")
    public BaseResponse<User> changeName(@RequestBody UserChangeRequest request) throws UserNotLoginException {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), userService.changeName(request));
    }
}
