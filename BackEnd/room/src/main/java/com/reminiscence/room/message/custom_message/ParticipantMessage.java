package com.reminiscence.room.message.custom_message;

import com.reminiscence.room.message.ResponseMessage;

public enum ParticipantMessage implements ResponseMessage {
    PARTICIPANT_WRITE_SUCCESS("참여자 추가가 완료되었습니다."),
    PARTICIPANT_DELETE_SUCCESS("참여자 삭제가 완료되었습니다.");

    private String message;
    @Override
    public String getMessage() {
        return message;
    }

    ParticipantMessage(String message) {
        this.message = message;
    }
}
