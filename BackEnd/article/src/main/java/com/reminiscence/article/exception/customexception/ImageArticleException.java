package com.reminiscence.article.exception.customexception;

import com.reminiscence.article.exception.BaseException;
import com.reminiscence.article.exception.message.ImageArticleExceptionMessage;

public class ImageArticleException extends BaseException {
    public ImageArticleException(ImageArticleExceptionMessage imageArticleExceptionMessage) {
        super(imageArticleExceptionMessage.getHttpStatus(), imageArticleExceptionMessage.getMessage());
    }
}
