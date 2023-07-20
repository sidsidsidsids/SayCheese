package com.reminiscence.article.notice.vo;

import com.reminiscence.article.domain.NoticeArticle;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class NoticeArticleListVo {

    private final Long id;
    private final String subject;
    private final String writer;
    private final Long hit;
    private final LocalDateTime createdDate;

    public NoticeArticleListVo(NoticeArticle noticeArticle){
        this.id=noticeArticle.getId();
        this.subject= noticeArticle.getSubject();
        this.writer=noticeArticle.getMember().getNickname();
        this.hit=noticeArticle.getHit();
        this.createdDate=noticeArticle.getCreatedDate();
    }


}
