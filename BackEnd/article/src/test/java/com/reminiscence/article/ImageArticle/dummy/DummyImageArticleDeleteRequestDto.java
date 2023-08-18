package com.reminiscence.article.ImageArticle.dummy;

public class DummyImageArticleDeleteRequestDto {
    private final String articleId;

    public static class Builder {
        private String articleId;

        public void imageId(String imageId) {
            this.articleId = imageId;
        }


        public DummyImageArticleDeleteRequestDto build() {
            return new DummyImageArticleDeleteRequestDto(this);
        }
    }

    private DummyImageArticleDeleteRequestDto(DummyImageArticleDeleteRequestDto.Builder builder) {
        this.articleId = builder.articleId;
    }

    public String getArticleId() {
        return articleId;
    }
}
