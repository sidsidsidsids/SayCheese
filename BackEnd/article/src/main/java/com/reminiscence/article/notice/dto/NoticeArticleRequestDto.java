package com.reminiscence.article.notice.dto;


import com.reminiscence.article.domain.NoticeArticle;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class NoticeArticleRequestDto {

    private String subject;
    private String content;



    public static NoticeArticle toEntity(NoticeArticleRequestDto dto) {
        return NoticeArticle.builder().
                subject(dto.getSubject()).
                content(dto.getContent()).
                build();
    }

}
