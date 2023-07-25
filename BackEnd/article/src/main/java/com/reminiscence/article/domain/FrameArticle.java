package com.reminiscence.article.domain;

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

}
