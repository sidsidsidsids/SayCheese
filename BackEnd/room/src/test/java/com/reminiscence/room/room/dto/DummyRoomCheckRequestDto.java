package com.reminiscence.room.room.dto;

public class DummyRoomCheckRequestDto {
    private String password;

    public static class Builder{
        private String password;

        public Builder password(String password){
            this.password = password;
            return this;
        }

        public DummyRoomCheckRequestDto build(){
            return new DummyRoomCheckRequestDto(this);
        }
    }

    public DummyRoomCheckRequestDto(DummyRoomCheckRequestDto.Builder builder){
        this.password = builder.password;
    }

    public String getPassword() {
        return password;
    }
}
