package com.study.file_test.dto;

import com.study.file_test.entity.FileInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FileDto {

    private long id; // 파일 ID
    private final String originalName; // 파일 원본 이름
    private final String saveName; // 저장된 파일 이름
    private final long size; // 파일 사이즈

    @Builder
    public FileDto(String originalName, String saveName, long size) {
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
    }

    public FileDto(FileInfo file) {
        this.id = file.getId();
        this.originalName = file.getOriginalName();
        this.saveName = file.getSaveName();
        this.size = file.getSize();
    }


    public FileInfo toEntity() {
        return FileInfo.builder()
                .originalName(originalName)
                .saveName(saveName)
                .size(size)
                .build();
    }
}
