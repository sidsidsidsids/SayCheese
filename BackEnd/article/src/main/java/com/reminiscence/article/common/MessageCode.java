package com.reminiscence.article.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Getter
public enum MessageCode {

    NOTICE_WRITE_SUCCESS(HttpStatus.OK, "공지글 작성이 완료되었습니다."),
    NOTICE_MODIFY_SUCCESS(HttpStatus.OK, "공지글 수정이 완료되었습니다.");


    MessageCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;

    public ResponseEntity<Response> toResponseEntity() throws JsonProcessingException {
        Response response=new Response(message);

        return new ResponseEntity<>(response, httpStatus);
    }

    public static class Response{
        private String message;

        private Response(String message){

            this.message=message;
        }

        public String getMessage(){

            return message;
        }
    }
}
