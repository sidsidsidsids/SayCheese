package com.reminiscence.article.message.custom_message;

import com.reminiscence.article.message.ResponseMessage;

public enum LoverResponseMessage implements ResponseMessage {

    LOVER_CLICK_SUCCESS("좋아요가 추가되었습니다.."),
    LOVER_CANCEL_MESSAGE("좋아요가 취소되었습니다.");

    private String message;

    LoverResponseMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
