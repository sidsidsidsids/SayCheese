package com.reminiscence.article.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("N")
@Table(name = "notice_article")
public class NoticeArticle extends Article {
    @Column(name = "subject")
    private String subject;
    @Column(name = "content")
    private String content;

    @Column(name="hit", nullable = false)
    private Long hit;
}
