package com.reminiscence.article.exception;


import com.reminiscence.article.exception.customexception.NoticeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse response=ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(),"서버와 연결이 원활하지 않습니다.");
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Notice 관련 예외 처리
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNoticeException(NoticeException noticeException) {
        ErrorResponse response=ErrorResponse.of(noticeException.getHttpStatus().value(),noticeException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, noticeException.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNoticeException(MethodArgumentNotValidException methodArgumentNotValidException) {
        ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
    }




}
