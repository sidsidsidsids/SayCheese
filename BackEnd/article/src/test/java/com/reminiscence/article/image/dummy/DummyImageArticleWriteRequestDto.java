package com.reminiscence.article.image.dummy;



public class DummyImageArticleWriteRequestDto {
    private final String imageId;
    public static class Builder {
        private String imageId;

        public void imageId(String imageId) {
            this.imageId = imageId;
        }


        public DummyImageArticleWriteRequestDto build() {
            return new DummyImageArticleWriteRequestDto(this);
        }
    }

    private DummyImageArticleWriteRequestDto(Builder builder) {
        this.imageId = builder.imageId;
    }

    public String getImageId() {
        return imageId;
    }
}
