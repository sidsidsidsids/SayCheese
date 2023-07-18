package com.reminiscence.article.exception.customexception;

import com.reminiscence.article.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NoticeException extends BaseException {

    public NoticeException(NoticeException noticeException) {
        super(noticeException.getHttpStatus(), noticeException.getMessage());
    }
}
