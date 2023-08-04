package com.reminiscence.article.exception;


import com.reminiscence.article.exception.customexception.*;
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
    // Image 관련 예외 처리
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleImageException(ImageException imageException) {
        ErrorResponse response=ErrorResponse.of(imageException.getHttpStatus().value(),imageException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, imageException.getHttpStatus());
    }
    // ImageArticle 관련 예외 처리
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleImageArticleException(ImageArticleException imageArticleException) {
        ErrorResponse response=ErrorResponse.of(imageArticleException.getHttpStatus().value(),imageArticleException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, imageArticleException.getHttpStatus());
    }
    // Member 관련 예외 처리
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException memberException) {
        ErrorResponse response = ErrorResponse.of(memberException.getHttpStatus().value(), memberException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, memberException.getHttpStatus());
    }

    // Notice 관련 예외 처리
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNoticeException(NoticeException noticeException) {
        ErrorResponse response=ErrorResponse.of(noticeException.getHttpStatus().value(),noticeException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, noticeException.getHttpStatus());
    }

    // Lover 관련 예외 처리
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleLoverException(LoverException loverException) {
        ErrorResponse response=ErrorResponse.of(loverException.getHttpStatus().value(),loverException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, loverException.getHttpStatus());
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleFrameArticleException(FrameArticleException frameArticleException) {
        ErrorResponse response=ErrorResponse.of(frameArticleException.getHttpStatus().value(),frameArticleException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, frameArticleException.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
    }




}
