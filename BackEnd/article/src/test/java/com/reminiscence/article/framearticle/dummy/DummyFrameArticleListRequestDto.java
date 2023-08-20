package com.reminiscence.article.framearticle.dummy;

public class DummyFrameArticleListRequestDto {
    private String searchWord;

    public static class Builder {
        private String searchWord;

        public Builder searchWord(String searchWord) {
            this.searchWord = searchWord;
            return this;
        }

        public DummyFrameArticleListRequestDto build() {
            return new DummyFrameArticleListRequestDto(this);
        }
    }

    private DummyFrameArticleListRequestDto(DummyFrameArticleListRequestDto.Builder builder) {
        this.searchWord = builder.searchWord;
    }

    public String getSearchWord() {
        return searchWord;
    }
}
