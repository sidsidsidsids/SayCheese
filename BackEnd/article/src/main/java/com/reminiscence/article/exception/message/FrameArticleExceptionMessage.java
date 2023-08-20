package com.reminiscence.article.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FrameArticleExceptionMessage {

    NOT_FOUND_DATA("프레임 글이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_FRAME_ARTICLE_LIST("해당 프레임 게시글 목록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_MODIFY_AUTH("글을 수정할 권한이 없습니다.",HttpStatus.FORBIDDEN),
    INVALID_DELETE_AUTH("글을 삭제할 권한이 없습니다.",HttpStatus.FORBIDDEN),
    NOT_FOUND_FRAME_ARTICLE("해당 프레임을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),;

    private final String message;
    private final HttpStatus httpStatus;



    private FrameArticleExceptionMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
