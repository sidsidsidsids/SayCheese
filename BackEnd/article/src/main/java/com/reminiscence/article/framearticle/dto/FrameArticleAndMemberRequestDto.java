package com.reminiscence.article.framearticle.dto;

import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FrameArticleAndMemberRequestDto {
    private Member member;
    private FrameArticleRequestDto frameArticleRequestDto;

    @Builder
    public FrameArticleAndMemberRequestDto(Member member, FrameArticleRequestDto frameArticleRequestDto) {
        this.member = member;
        this.frameArticleRequestDto = frameArticleRequestDto;
    }

    public static Frame toEntity(FrameArticleRequestDto requestDto, String link) {
        String type=requestDto.getName().substring(requestDto.getName().lastIndexOf('.')+1);
        String name = requestDto.getName().substring(requestDto.getName().lastIndexOf(".")+1);
        Character openYn=requestDto.getIsPublic()?'Y':'N';
        return Frame.builder()
                .name(name)
                .link(link)
                .frameSpecification(requestDto.getFrameSpecification())
                .type(type)
                .open_yn(openYn)
                .build();
    }
}
