package com.reminiscence.room.room.dto;

import com.reminiscence.room.domain.Mode;
import com.reminiscence.room.domain.Specification;
import lombok.Getter;

@Getter
public class RoomInfoResponseDto {
    private Specification specification;
    private Mode mode;

    public RoomInfoResponseDto(Specification specification, Mode mode) {
        this.specification = specification;
        this.mode = mode;
    }
}
