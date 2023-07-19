package com.reminiscence.article.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "image_article")
@DiscriminatorValue("I")
@Getter
public class ImageArticle extends Article{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

}
