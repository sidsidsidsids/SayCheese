package com.reminiscence.room.exception;


import com.reminiscence.room.exception.customexception.MemberException;
import com.reminiscence.room.exception.customexception.ParticipantException;
import com.reminiscence.room.exception.customexception.RoomException;
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
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException memberException) {
        ErrorResponse response = ErrorResponse.of(memberException.getHttpStatus().value(), memberException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, memberException.getHttpStatus());
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleRoomException(RoomException roomException) {
        ErrorResponse response = ErrorResponse.of(roomException.getHttpStatus().value(), roomException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, roomException.getHttpStatus());
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleParticipantException(ParticipantException participantException) {
        ErrorResponse response = ErrorResponse.of(participantException.getHttpStatus().value(), participantException.getMessage());
        return new ResponseEntity<ErrorResponse>(response, participantException.getHttpStatus());
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
    }


}
