package com.reminiscence.article.image.dummy;

public class DummyDeleteImageOwnerRequestDto {

    private Long imageId;

    public static class Builder{
        private Long imageId;

        public void imageId(Long imageId){
            this.imageId = imageId;
        }

        public DummyDeleteImageOwnerRequestDto build(){
            return new DummyDeleteImageOwnerRequestDto(this);
        }
    }

    public DummyDeleteImageOwnerRequestDto(DummyDeleteImageOwnerRequestDto.Builder builder){
        this.imageId = builder.imageId;
    }

    public Long getImageId() {
        return imageId;
    }
}
