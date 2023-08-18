package com.reminiscense.batch.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
public class ImageTagKey implements Serializable {
    private Long imageId;
    private Long tagId;

    public ImageTagKey(Long imageId, Long tagId) {
        this.imageId = imageId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageTagKey that = (ImageTagKey) o;
        return Objects.equals(getImageId(), that.getImageId()) && Objects.equals(getTagId(), that.getTagId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImageId(), getTagId());
    }
}
