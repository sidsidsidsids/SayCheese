package com.reminiscence.article.framearticle.dummy;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.reminiscence.article.domain.FrameSpecification;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class DummyFrameArticleRequestDto {
    private String name;
    private String link;
    private Boolean isPublic;
    private String frameSpecification;
    private String subject;

    public static class Builder {
        private String name;
        private String link;
        private Boolean isPublic;
        private String frameSpecification;
        private String subject;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public Builder isPublic(Boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        public Builder frameSpecification(String frameSpecification) {
            this.frameSpecification = frameSpecification;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public DummyFrameArticleRequestDto build() {
            return new DummyFrameArticleRequestDto(this);
        }
    }

    private DummyFrameArticleRequestDto(Builder builder) {
        this.name = builder.name;
        this.link = builder.link;
        this.isPublic = builder.isPublic;
        this.frameSpecification = builder.frameSpecification;
        this.subject = builder.subject;
    }


    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public String getFrameSpecification() {
        return frameSpecification;
    }

    public String getSubject() {
        return subject;
    }

}
