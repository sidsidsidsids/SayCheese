package com.reminiscence.exception.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberExceptionMessage {
    DATA_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_JOIN_FAILURE_NICKNAME_PROTECTED( "닉네임 앞에 GUEST를 사용할 수 없습니다.",HttpStatus.ACCEPTED),
    MEMBER_PASSWORD_CONFIRM_FAILURE( "패스워드가 다릅니다.",HttpStatus.BAD_REQUEST),
    MEMBER_JOIN_FAILURE_EMAIL_DUPLICATED( "아이디가 중복됩니다.",HttpStatus.ALREADY_REPORTED),
    MEMBER_JOIN_FAILURE_NICKNAME_DUPLICATED( "닉네임이 중복됩니다.",HttpStatus.CONFLICT);


    private final String message;
    private final HttpStatus httpStatus;

    MemberExceptionMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
