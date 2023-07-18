package com.reminiscence.article.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ImageTag {

    @EmbeddedId
    private ImageTagKey imageTagKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="imageId")
    @MapsId("imageId")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tagId")
    @MapsId("tagId")
    private Tag tag;
}
