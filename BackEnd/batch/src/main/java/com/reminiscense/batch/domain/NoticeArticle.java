package com.reminiscense.batch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@DiscriminatorValue("N")
@Table(name = "notice_article")
@NoArgsConstructor
@Getter
public class NoticeArticle extends Article {
    @Column(name = "subject")
    private String subject;
    @Column(name = "content")
    private String content;

    @Column(name="hit", nullable = false)
    private Long hit;
    @Builder
    public NoticeArticle(String subject, String content,Member member) {

        this(subject,content,0L,member);
    }
    public NoticeArticle(String subject, String content, Long hit, Member member) {
        super(member);
        this.subject = subject;
        this.content = content;
        this.hit=hit;
    }


    public void modifyContent(String content) {
        this.content=content;
    }
    public void modifySubject(String subject) {
        this.subject=subject;
    }
}
