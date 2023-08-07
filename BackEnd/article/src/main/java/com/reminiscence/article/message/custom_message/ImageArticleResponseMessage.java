package com.reminiscence.article.message.custom_message;

import com.reminiscence.article.message.ResponseMessage;

public enum ImageArticleResponseMessage implements ResponseMessage {

    /**
     * 이미지 게시글 응답 메시지 정의
     */
    IMAGE_ARTICLE_WRITE_SUCCESS( "이미지 게시글 작성이 완료되었습니다."),
    IMAGE_ARTICLE_DELETE_SUCCESS( "이미지 게시글 삭제가 완료되었습니다."),
    IMAGE_ARTICLE_IMAGE_DELETE_SUCCESS( "이미지 게시글 및 이미지 삭제가 완료되었습니다.");

    private final String message;

    ImageArticleResponseMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
