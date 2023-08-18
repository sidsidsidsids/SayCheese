package com.reminiscence.room.participant.dto;

public class DummyDeleteParticipantRequestDto {
    private String roomCode;
    public static class Builder{
        private String roomCode;

        public Builder roomCode(String roomCode){
            this.roomCode = roomCode;
            return this;
        }

        public DummyDeleteParticipantRequestDto build(){
            return new DummyDeleteParticipantRequestDto(this);
        }
    }

    private DummyDeleteParticipantRequestDto(DummyDeleteParticipantRequestDto.Builder builder){
        this.roomCode = builder.roomCode;
    }

    public String getRoomCode() {
        return roomCode;
    }
}
