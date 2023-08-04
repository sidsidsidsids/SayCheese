package com.reminiscence.article.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@DiscriminatorValue("F")
@Table(name = "frame_article")
@NoArgsConstructor
@Getter
public class FrameArticle extends Article{
    @Column(name="subject", nullable = false)
    private String subject;


    @OneToOne(fetch = FetchType.LAZY)
    private Frame frame;

    @Builder
    public FrameArticle(String subject, Frame frame,Member member) {
        super(member);
        this.subject = subject;
        this.frame = frame;
    }

}
