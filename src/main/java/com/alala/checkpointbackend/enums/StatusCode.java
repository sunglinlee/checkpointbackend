package com.alala.checkpointbackend.enums;

import lombok.Getter;

@Getter
public enum StatusCode {
    SUCCESS("0000"),
    DUPLICATE_USER("1001"),
    WRONG_PASSWORD("1002"),
    USER_NOT_LOGIN("1003"),;

    private final String code;

    StatusCode(String code) {
        this.code = code;
    }
}
