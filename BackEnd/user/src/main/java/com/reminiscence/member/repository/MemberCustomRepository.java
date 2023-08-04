package com.reminiscence.member.repository;

import com.reminiscence.domain.Member;
import com.reminiscence.member.dto.MemberSearchResponseDto;

import java.util.List;

public interface MemberCustomRepository {
    List<MemberSearchResponseDto> searchMembers(String key);
}
