package com.reminiscence.room.room.dto;

import lombok.Builder;


public class DummyRoomCheckRequestDto {
    private String password;

    @Builder
    public DummyRoomCheckRequestDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
