package com.reminiscence.article.exception.customexception;

import com.reminiscence.article.exception.BaseException;
import com.reminiscence.article.exception.message.ImageExceptionMessage;

public class ImageException extends BaseException {
    public ImageException(ImageExceptionMessage imageExceptionMessage) {
        super(imageExceptionMessage.getHttpStatus(), imageExceptionMessage.getMessage());
    }
}
