package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import com.reminiscence.domain.Role;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class MemberSearchResponseDto implements Serializable {

    private String email;
    private String nickname;

    public MemberSearchResponseDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .build();
    }
}
