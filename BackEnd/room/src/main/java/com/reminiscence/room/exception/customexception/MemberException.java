package com.reminiscence.room.exception.customexception;

import com.reminiscence.room.exception.BaseException;
import com.reminiscence.room.exception.message.MemberExceptionMessage;

public class MemberException extends BaseException {
    public MemberException(MemberExceptionMessage message) {
        super(message.getHttpStatus(), message.getMessage());
    }

}
