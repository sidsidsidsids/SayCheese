package com.reminiscence.article.exception.customexception;

import com.reminiscence.article.exception.BaseException;
import com.reminiscence.article.exception.message.NoticeMessage;
import org.springframework.http.HttpStatus;

public class NoticeException extends BaseException {

    public NoticeException(NoticeMessage noticeMessage) {
        super(noticeMessage.getHttpStatus(), noticeMessage.getMessage());
    }
}
