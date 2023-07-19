package com.reminiscence.article.exception.message;

import com.reminiscence.article.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NoticeMessage {
    DATA_NOT_FOUND("공지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);


    private final String message;
    private final HttpStatus httpStatus;

    NoticeMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
