package com.reminiscence.article.exception.customexception;

import com.reminiscence.article.exception.BaseException;
import com.reminiscence.article.exception.message.FrameArticleExceptionMessage;

public class FrameArticleException extends BaseException {
    public FrameArticleException(FrameArticleExceptionMessage frameArticleExceptionMessage){
        super(frameArticleExceptionMessage.getHttpStatus(),frameArticleExceptionMessage.getMessage());
    }
}
