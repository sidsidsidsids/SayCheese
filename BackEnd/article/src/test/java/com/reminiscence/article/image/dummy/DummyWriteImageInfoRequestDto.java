package com.reminiscence.article.image.dummy;

import lombok.Builder;

import java.util.List;


@Builder
public class DummyWriteImageInfoRequestDto {
    private String imageName;
    private List<Long> tags;
    private String roomCode;

    public String getImageName() {
        return imageName;
    }

    public List<Long> getTags() {
        return tags;
    }

    public String getRoomCode() {
        return roomCode;
    }
}
