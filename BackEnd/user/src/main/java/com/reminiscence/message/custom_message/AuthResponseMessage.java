package com.reminiscence.message.custom_message;

import com.reminiscence.message.ResponseMessage;
import lombok.Getter;

@Getter
public enum AuthResponseMessage implements ResponseMessage {

    ACCESS_TOKEN_REISSUE_SUCCESS ("AccessToken reissue success"),
    ACCESS_TOKEN_REISSUE_FAIL ("다시 로그인 해주세요.");

    private final String message;

    private AuthResponseMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}


