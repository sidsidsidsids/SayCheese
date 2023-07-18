package com.reminiscence.article.domain;

import com.reminiscence.article.domain.Article;

import javax.persistence.*;

@Entity
@DiscriminatorValue("N")
@Table(name = "notice_article")
public class Notice extends Article {
    @Column(name = "subject")
    private String subject;
    @Column(name = "content")
    private String content;

    @Column(name="hit", nullable = false)
    private Long hit;
}
