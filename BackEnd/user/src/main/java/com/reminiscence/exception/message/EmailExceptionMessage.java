package com.reminiscence.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EmailExceptionMessage {
    DATA_NOT_FOUND("관련 데이터를 찾을 수 없습니다.",HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

    private EmailExceptionMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
