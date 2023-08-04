package com.reminiscence.room.participant.dto;

public class DummyWriteParticipantRequestDto {

    private String ownerYn;
    private String roomCode;

    public static class Builder{
        private String ownerYn;
        private String roomCode;


        public Builder ownerYn(String ownerYn){
            this.ownerYn = ownerYn;
            return this;
        }

        public Builder roomCode(String roomCode){
            this.roomCode = roomCode;
            return this;
        }

        public DummyWriteParticipantRequestDto build(){
            return new DummyWriteParticipantRequestDto(this);
        }
    }

    private DummyWriteParticipantRequestDto(DummyWriteParticipantRequestDto.Builder builder){
        this.ownerYn = builder.ownerYn;
        this.roomCode = builder.roomCode;
    }

    public String getOwnerYn() {
        return ownerYn;
    }

    public String getRoomCode() {
        return roomCode;
    }
}
