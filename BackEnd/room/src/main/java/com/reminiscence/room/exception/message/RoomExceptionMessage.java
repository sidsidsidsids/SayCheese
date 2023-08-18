package com.reminiscence.room.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum RoomExceptionMessage {
    NOT_FOUND_ROOM("해당 방에 접근하실 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_MATCH_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_SESSION("해당 세션을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    ROOM_IS_FULL("방이 꽉 찼습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_IN_ROOM("이미 방에 접속중입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    RoomExceptionMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
