package com.reminiscence.member.dto;

import com.reminiscence.message.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberProfileResponseDto {
    private String profile;
    private Response response;
}
