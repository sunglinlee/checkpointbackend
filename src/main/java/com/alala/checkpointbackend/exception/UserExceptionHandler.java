package com.alala.checkpointbackend.exception;

import com.alala.checkpointbackend.enums.StatusCode;
import com.alala.checkpointbackend.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Order(1)
@ControllerAdvice(basePackages = {"com.alala.checkpointbackend.controller"})
@RequiredArgsConstructor
public class UserExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DuplicateUserException.class)
    public BaseResponse<String> duplicateUser() {
        return new BaseResponse<>(StatusCode.DUPLICATE_USER.getCode(), "Duplicate User");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(WrongPasswordException.class)
    public BaseResponse<String> wrongPassword() {
        return new BaseResponse<>(StatusCode.WRONG_PASSWORD.getCode(), "Wrong Password");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UserNotLoginException.class)
    public BaseResponse<String> userNotLogin() {
        return new BaseResponse<>(StatusCode.USER_NOT_LOGIN.getCode(), "User Not Login");
    }

}
