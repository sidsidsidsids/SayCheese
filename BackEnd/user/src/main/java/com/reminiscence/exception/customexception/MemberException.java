package com.reminiscence.exception.customexception;

import com.reminiscence.exception.BaseException;
import com.reminiscence.exception.message.MemberExceptionMessage;

public class MemberException extends BaseException {

    public MemberException(MemberExceptionMessage memberExceptionMessage) {
        super(memberExceptionMessage.getHttpStatus(), memberExceptionMessage.getMessage());
    }
}
