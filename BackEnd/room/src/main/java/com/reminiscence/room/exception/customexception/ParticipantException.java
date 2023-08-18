package com.reminiscence.room.exception.customexception;

import com.reminiscence.room.exception.BaseException;
import com.reminiscence.room.exception.message.ParticipantExceptionMessage;

public class ParticipantException extends BaseException {


    public ParticipantException(ParticipantExceptionMessage message) {
        super(message.getHttpStatus(), message.getMessage());
    }
}
