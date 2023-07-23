package com.reminiscence.article.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ImageExceptionMessage {

    NOT_FOUND_IMAGE("해당 이미지를 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    NOT_FOUND_IMAGE_TAG("해당 이미지의 태그를 찾을 수 없습니다.",HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;



    private ImageExceptionMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }


}
