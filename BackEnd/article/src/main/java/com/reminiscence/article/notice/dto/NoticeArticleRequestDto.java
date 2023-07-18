package com.reminiscence.article.notice.dto;


import com.reminiscence.article.domain.NoticeArticle;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class NoticeArticleRequestDto {

    @Size(min=3, max=20, message="제목은 최소 3글자, 20글자 이하로 작성하세요")
    @NotBlank(message= "제목을 입력하세요")
    private String subject;
    @Size(min=3, max=1000, message="내용은 3글자 이상, 1000글자 이하로 작성해주세요")
    @NotBlank(message = "내용을 입력하세요")
    private String content;

}
