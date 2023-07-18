package com.reminiscence.article.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public enum MessageCode {

    NOTICE_WRITE_SUCCESS(HttpStatus.OK, "공지글 작성이 완료되었습니다.");


    MessageCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;

    public ResponseEntity<String> toResponseEntity() {
        return new ResponseEntity<>(message, httpStatus);
    }
}
