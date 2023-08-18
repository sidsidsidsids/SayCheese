package com.reminiscence.message.custom_message;

import com.reminiscence.message.ResponseMessage;

public enum EmailResponseMessage implements ResponseMessage {
    EMAIL_SEND_SUCCESS("이메일 전송에 성공하였습니다."),
    EMAIL_AUTH_SUCCESS("이메일 인증이 성공적으로 이루어졌습니다.");
    private String message;

    EmailResponseMessage(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
