package com.reminiscence.article.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FrameSpecification {
    VERTICAL, HORIZONTAL;

    @JsonCreator
    public static FrameSpecification from(String value) {
        for (FrameSpecification specification : FrameSpecification.values()) {
            if (specification.name().equals(value)) {
                return specification;
            }
        }
        return null;
    }
}
