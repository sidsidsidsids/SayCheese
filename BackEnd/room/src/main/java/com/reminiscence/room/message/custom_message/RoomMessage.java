package com.reminiscence.room.message.custom_message;

import com.reminiscence.room.message.ResponseMessage;

public enum RoomMessage implements ResponseMessage {
    ROOM_WRITE_SUCCESS("방 생성이 완료되었습니다."),
    ROOM_DELETE_SUCCESS("방 삭제가 완료되었습니다."),
    ROOM_CONNECTION_ABLE("방 입장이 가능하십니다."),
    ROOM_START_SUCCESS("방 시작이 완료되었습니다."),
    ROOM_CHECK_SUCCESS("방 확인이 완료되었습니다."),;

    private String message;

    RoomMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
