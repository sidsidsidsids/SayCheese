package com.reminiscence.article.exception.customexception;

import com.reminiscence.article.exception.BaseException;
import com.reminiscence.article.exception.message.LoverExceptionMessage;

public class LoverException extends BaseException {
    public LoverException(LoverExceptionMessage loverExceptionMessage) {
        super(loverExceptionMessage.getHttpStatus(), loverExceptionMessage.getMessage());
    }
}
