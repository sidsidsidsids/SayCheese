package com.reminiscence.article.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberExceptionMessage {

    NOT_FOUND_MEMBER("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_MATCH_MEMBER("해당 유저와 일치하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;



    private MemberExceptionMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
