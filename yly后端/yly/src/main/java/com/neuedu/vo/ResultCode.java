package com.neuedu.vo;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200),
    FAILED(500),
    UNAUTHORIZED(401),
    FORBID(403);
    private Integer value;
    ResultCode(Integer value) {
        this.value = value;
    }
}
