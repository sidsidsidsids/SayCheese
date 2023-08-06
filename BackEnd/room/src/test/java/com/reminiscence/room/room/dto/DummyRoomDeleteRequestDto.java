package com.reminiscence.room.room.dto;

public class DummyRoomDeleteRequestDto {
    private String roomCode;

    public static class Builder{
        private String roomCode;

        public Builder roomCode(String roomCode){
            this.roomCode = roomCode;
            return this;
        }

        public DummyRoomDeleteRequestDto build(){
            return new DummyRoomDeleteRequestDto(this);
        }
    }

    public DummyRoomDeleteRequestDto(DummyRoomDeleteRequestDto.Builder builder){
        this.roomCode = builder.roomCode;
    }

    public String getRoomCode() {
        return roomCode;
    }
}
