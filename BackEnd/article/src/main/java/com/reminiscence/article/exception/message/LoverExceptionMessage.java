package com.reminiscence.article.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LoverExceptionMessage {
    NOT_FOUND_LOVER("해당 좋아요를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_ARTICLE_TYPE("해당 게시글 타입을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;



    private LoverExceptionMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
