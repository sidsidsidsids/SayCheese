package com.reminiscence.room.room.dto;

import com.reminiscence.room.domain.Mode;

public class DummyRoomCreateRequestDto {
    private String password;
    private Integer maxCount;
    private Mode mode;
    private String roomCode;
    private String specification;

    public static class Builder{
        private String password;
        private Integer maxCount;
        private Mode mode;
        private String roomCode;
        private String specification;

        public Builder password(String password){
            this.password = password;
            return this;
        }

        public Builder maxCount(Integer maxCount){
            this.maxCount = maxCount;
            return this;
        }

        public Builder mode(Mode mode){
            this.mode = mode;
            return this;
        }

        public Builder roomCode(String roomCode){
            this.roomCode = roomCode;
            return this;
        }

        public Builder specification(String specification){
            this.specification = specification;
            return this;
        }


        public DummyRoomCreateRequestDto build(){
            return new DummyRoomCreateRequestDto(this);
        }
    }

    public DummyRoomCreateRequestDto(DummyRoomCreateRequestDto.Builder builder){
        this.password = builder.password;
        this.maxCount = builder.maxCount;
        this.mode = builder.mode;
        this.roomCode = builder.roomCode;
        this.specification = builder.specification;
    }

    public String getPassword() {
        return password;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public Mode getMode() {
        return mode;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getSpecification() {
        return specification;
    }
}
