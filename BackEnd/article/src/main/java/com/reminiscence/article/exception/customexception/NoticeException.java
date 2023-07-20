package com.reminiscence.article.exception.customexception;

import com.reminiscence.article.exception.BaseException;
import com.reminiscence.article.exception.message.NoticeExceptionMessage;

public class NoticeException extends BaseException {

    public NoticeException(NoticeExceptionMessage noticeExceptionMessage) {
        super(noticeExceptionMessage.getHttpStatus(), noticeExceptionMessage.getMessage());
    }
}
