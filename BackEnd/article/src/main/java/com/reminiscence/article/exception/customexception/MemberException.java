package com.reminiscence.article.exception.customexception;

import com.reminiscence.article.exception.BaseException;
import com.reminiscence.article.exception.message.MemberExceptionMessage;

public class MemberException extends BaseException {

        public MemberException(MemberExceptionMessage memberExceptionMessage) {
            super(memberExceptionMessage.getHttpStatus(), memberExceptionMessage.getMessage());
        }
}
