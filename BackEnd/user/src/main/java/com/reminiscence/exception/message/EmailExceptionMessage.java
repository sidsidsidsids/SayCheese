package com.reminiscence.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EmailExceptionMessage {
    ;

    private final String message;
    private final HttpStatus httpStatus;

    private EmailExceptionMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
