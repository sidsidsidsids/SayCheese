package com.reminiscence.article.domain;

import com.reminiscence.article.domain.Article;

import javax.persistence.*;

@Entity
@DiscriminatorValue("N")
@PrimaryKeyJoinColumn(name = "article_id")
public class Notice extends Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_id", insertable = false, updatable = false)
    private Long articleId;

    @Column(name = "subject")
    private String subject;
    @Column(name = "content")
    private String content;

    @Column(name="hit", nullable = false)
    private Long hit;
}
