package com.reminiscence.article.amazon.dto;

public class DummyPreSignRequestDto {
    private String fileName;
    private FileType fileType;

    public static class Builder{
        private String fileName;
        private FileType fileType;

        public void fileName(String fileName){
            this.fileName = fileName;
        }
        public void fileType(FileType fileType){
            this.fileType = fileType;
        }
        public DummyPreSignRequestDto build(){
            return new DummyPreSignRequestDto(this);
        }
    }

    private DummyPreSignRequestDto(DummyPreSignRequestDto.Builder builder) {
        this.fileName = builder.fileName;
        this.fileType = builder.fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public FileType getFileType() {
        return fileType;
    }
}
