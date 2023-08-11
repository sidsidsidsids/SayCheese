package com.reminiscence.member.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class MemberProfileSaveRequestDto {

    @NotBlank(message = "이미지 파일 이름을 입력해주세요.")
    private String imageName;

}
