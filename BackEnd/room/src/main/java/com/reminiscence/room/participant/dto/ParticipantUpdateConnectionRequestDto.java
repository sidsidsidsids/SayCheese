package com.reminiscence.room.participant.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ParticipantUpdateConnectionRequestDto {
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
}
