package com.reminiscence.exception.customexception;

import com.reminiscence.exception.BaseException;
import com.reminiscence.exception.message.EmailExceptionMessage;

public class EmailException extends BaseException {

    public EmailException(EmailExceptionMessage emailExceptionMessage) {
        super(emailExceptionMessage.getHttpStatus(), emailExceptionMessage.getMessage());
    }
}
