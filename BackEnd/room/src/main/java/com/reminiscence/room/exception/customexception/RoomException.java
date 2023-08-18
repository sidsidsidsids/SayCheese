package com.reminiscence.room.exception.customexception;

import com.reminiscence.room.exception.BaseException;
import com.reminiscence.room.exception.message.RoomExceptionMessage;

public class RoomException extends BaseException {
    public RoomException(RoomExceptionMessage message) {
        super(message.getHttpStatus(), message.getMessage());
    }
}
