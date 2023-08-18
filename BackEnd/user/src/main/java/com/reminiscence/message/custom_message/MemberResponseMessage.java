package com.reminiscence.message.custom_message;

import com.reminiscence.message.ResponseMessage;
import lombok.Getter;

@Getter
public enum MemberResponseMessage implements ResponseMessage {

    MEMBER_JOIN_SUCCESS( "회원 가입이 완료되었습니다."),
    MEMBER_UPDATE_SUCCESS( "회원 정보 수정이 완료되었습니다."),
    MEMBER_ID_CHECK_SUCCESS( "사용 가능한 아이디입니다."),
    MEMBER_ID_CHECK_FAILURE( "이미 사용중인 아이디입니다."),
    MEMBER_NICKNAME_CHECK_SUCCESS( "사용 가능한 닉네임입니다."),
    MEMBER_NICKNAME_CHECK_FAILURE( "이미 사용중인 닉네임입니다."),
    MEMBER_PROFILE_MODIFY_SUCCESS( "프로필이 수정되었습니다."),
    MEMBER_EMAIL_CHECK_REQUEST( "해당 이메일로 가입된 정보가 없습니다.\n이메일을 다시 확인해주세요."),
    MEMBER_PASSWORD_FIND_EMAIL_SEND_SUCCESS( "비밀번호 인증 코드가 메일로 전송되었습니다."),
    MEMBER_PASSWORD_FIND_EMAIL_AUTHENTICATE_SUCCESS( "인증되었습니다."),
    MEMBER_PASSWORD_MODIFY_SUCCESS( "비밀번호가 변경되었습니다."),
    MEMBER_DELETE_SUCCESS( "회원 탈퇴가 완료되었습니다.");


    private final String message;

    MemberResponseMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
