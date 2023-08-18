package com.reminiscence.room.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int httpStatus;
    private final String message;

    private ErrorResponse(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ErrorResponse of(int httpStatus, String message) {
        return new ErrorResponse(httpStatus, message);
    }
}
