package com.reminiscence.article.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FrameSpecification {
    A,B;

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
