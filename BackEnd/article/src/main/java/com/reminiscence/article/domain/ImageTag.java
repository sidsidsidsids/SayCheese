package com.reminiscence.article.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public ImageTag(Image image, Tag tag) {
        this.image = image;
        this.tag = tag;
        image.addImageTag(this);
        tag.addImageTag(this);
        this.imageTagKey = new ImageTagKey(image.getId(), tag.getId());
    }
    public ImageTag(Image image, Long tagId) {
        this.image = image;
        image.addImageTag(this);
        this.imageTagKey = new ImageTagKey(image.getId(), tagId);
    }
}
