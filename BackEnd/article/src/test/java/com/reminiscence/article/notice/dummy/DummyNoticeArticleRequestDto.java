package com.reminiscence.article.notice.dummy;

public class DummyNoticeArticleRequestDto {
    private String subject;
    private String content;
    public static class Builder {
        private String subject;
        private String content;

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public DummyNoticeArticleRequestDto build() {
            return new DummyNoticeArticleRequestDto(this);
        }
    }

    private DummyNoticeArticleRequestDto(Builder builder) {
        this.subject = builder.subject;
        this.content = builder.content;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }


}
