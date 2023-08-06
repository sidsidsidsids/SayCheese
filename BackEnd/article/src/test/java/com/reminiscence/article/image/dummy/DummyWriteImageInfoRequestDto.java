package com.reminiscence.article.image.dummy;

public class DummyWriteImageInfoRequestDto {
    private String imageLink;
    private String imageName;

    public static class Builder{
        private String imageLink;
        private String imageName;

        public void imageLink(String imageLink){
            this.imageLink = imageLink;
        }
        public void imageName(String imageName){
            this.imageName = imageName;
        }
        public DummyWriteImageInfoRequestDto build(){
            return new DummyWriteImageInfoRequestDto(this);
        }
    }

    public DummyWriteImageInfoRequestDto(DummyWriteImageInfoRequestDto.Builder builder) {
        this.imageLink = builder.imageLink;
        this.imageName = builder.imageName;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getImageName() {
        return imageName;
    }
}
