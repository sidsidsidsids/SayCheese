package com.reminiscence.article.message.custom_message;

import com.reminiscence.article.message.ResponseMessage;

public enum ImageResponseMessage implements ResponseMessage {
    INSERT_IMAGE_SUCCESS("이미지 저장 성공"),
    DELETE_IMAGE_SUCCESS("이미지 삭제 성공");

    private String message;

    ImageResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
