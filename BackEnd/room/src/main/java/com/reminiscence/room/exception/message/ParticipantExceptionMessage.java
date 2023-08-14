package com.reminiscence.room.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ParticipantExceptionMessage {
    NOT_FOUND_PARTICIPANT("해당 참여자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_OWNER_PARTICIPANT("해당 방의 방장을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

    private ParticipantExceptionMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
