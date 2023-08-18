package com.reminiscence.room.room.dto;

import com.reminiscence.room.domain.Mode;
import com.reminiscence.room.domain.Specification;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DummyRoomCreateRequestDto {
    private String password;
    private Integer maxCount;
    private Mode mode;
    private String roomCode;
    private Specification specification;

    public static class Builder {
        private String password;
        private Integer maxCount;
        private Mode mode;
        private String roomCode;
        private Specification specification;
    }
}
