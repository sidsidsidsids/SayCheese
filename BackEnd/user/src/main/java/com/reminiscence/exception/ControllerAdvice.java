package com.reminiscence.exception;

import com.reminiscence.exception.customexception.EmailException;


import com.reminiscence.exception.customexception.MemberException;
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
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleEmailException(EmailException emailException) {
        ErrorResponse response=ErrorResponse.of(emailException.getHttpStatus().value(),emailException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, emailException.getHttpStatus());
    }


    // Member 관련 예외 처리
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException memberException) {
        ErrorResponse response=ErrorResponse.of(memberException.getHttpStatus().value(),memberException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, memberException.getHttpStatus());
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
    }


}