package com.reminiscense.batch.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "image_article")
@DiscriminatorValue("I")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageArticle extends Article{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    // 생성 메서드

    @Builder
    public ImageArticle(Member member, Image image){
        super(member);
        this.image = image;
    }

}
