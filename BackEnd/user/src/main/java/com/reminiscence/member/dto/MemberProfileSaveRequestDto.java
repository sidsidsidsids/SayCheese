package com.reminiscence.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberProfileSaveRequestDto {

    @NotBlank(message = "파일 이름을 입력해주세요.")
    private String profileName;

    @Builder
    public MemberProfileSaveRequestDto (String profileName) {
        this.profileName = profileName;
    }

}
