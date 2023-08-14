package com.reminiscence.member.dto;

import com.reminiscence.message.Response;
import com.reminiscence.message.custom_message.MemberResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberFindPasswordResponseDto {
    MemberResponseMessage message;
}
