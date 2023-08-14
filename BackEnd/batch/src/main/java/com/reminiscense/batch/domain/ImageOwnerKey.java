package com.reminiscense.batch.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
public class ImageOwnerKey implements Serializable {
    private Long imageId;
    private Long memberId;

    public ImageOwnerKey(Long imageId, Long memberId) {
        this.imageId = imageId;
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageOwnerKey that = (ImageOwnerKey) o;
        return Objects.equals(getImageId(), that.getImageId()) && Objects.equals(getMemberId(), that.getMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImageId(), getMemberId());
    }
}
