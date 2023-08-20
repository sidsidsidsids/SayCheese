package com.reminiscence.article.message.custom_message;

import com.reminiscence.article.message.ResponseMessage;

public enum FrameResponseMessage implements ResponseMessage {
    FRAME_WRITE_SUCCESS("글이 성공적으로 작성되었습니다."),
    FRAME_DELETE_SUCCESS("프레임 글이 성공적으로 삭제되었습니다."),
    FRAME_MODIFY_PUBLIC_SUCCESS("프레임 글 공개 여부가 변경되었습니다."),
    FRAME_MODIFY_NOT_PUBLIC_SUCCESS("프레임 글 공개 여부가 변경되었습니다.");
    private final String message;

    private FrameResponseMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {

        return message;
    }
}
