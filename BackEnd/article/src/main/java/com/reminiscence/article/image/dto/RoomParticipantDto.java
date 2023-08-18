package com.reminiscence.article.image.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoomParticipantDto {
    private Long memberId;

    @JsonCreator
    public RoomParticipantDto(@JsonProperty("memberId")Long memberId) {
        this.memberId = memberId;
    }
}
