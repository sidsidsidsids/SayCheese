package com.reminiscence.email.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class EmailCheckRequestDto {
    @Email(regexp = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", message ="이메일 양식이 맞지 않습니다")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    
    @NotBlank(message = "토큰 값이 존재하지 않습니다")
    private String token;
}
