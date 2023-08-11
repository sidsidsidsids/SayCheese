package com.reminiscence.article.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ImageArticleExceptionMessage {


    INVALID_DELETE_YN("잘못된 삭제 요청이 들어왔습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_IMAGE_ARTICLE("해당 이미지 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_IMAGE_ARTICLE_LIST("해당 이미지 게시글 목록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_IMAGE_TAG("해당 이미지 태그를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_ARTICLE("해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

    private ImageArticleExceptionMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
