package com.reminiscence.article.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.reminiscence.article.frame.dto.FrameType;

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
