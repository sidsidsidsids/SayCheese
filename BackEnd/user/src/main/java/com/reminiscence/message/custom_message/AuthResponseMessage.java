package com.reminiscence.message.custom_message;

import com.reminiscence.message.ResponseMessage;
import lombok.Getter;

@Getter
public enum AuthResponseMessage implements ResponseMessage {

    ACCESS_TOKEN_REISSUE_SUCCESS ("AccessToken reissue success"),
    ACCESS_TOKEN_REISSUE_FAIL ("다시 로그인 해주세요."),
    GUEST_TOKEN_ISSUE_SUCCESS("게스트 토큰 발급 성공");

    private final String message;

    AuthResponseMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}


