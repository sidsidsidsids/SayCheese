package com.reminiscence.article.message.custom_message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reminiscence.article.message.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public enum NoticeResponseMessage implements ResponseMessage {

    NOTICE_WRITE_SUCCESS( "공지글 작성이 완료되었습니다."),
    NOTICE_MODIFY_SUCCESS( "공지글 수정이 완료되었습니다.");

    private final String message;

    private NoticeResponseMessage(String message) {
        this.message = message;
    }


    @Override
    public String getMessage() {
        return message;
    }




}
