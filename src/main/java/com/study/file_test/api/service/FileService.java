package com.study.file_test.api.service;


import com.study.file_test.api.repository.FileRepository;
import com.study.file_test.dto.FileDto;
import com.study.file_test.entity.FileInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${file.dir}")
    private String filePath;

    private final FileRepository fileRepository;

    // 파일 목록 조회
    @Transactional
    public List<FileInfo> fileList() {
        return fileRepository.findAll();
    }

    // 파일 정보 저장
    @Transactional
    public boolean save(MultipartFile file) {
        FileDto dto = saveFile(file);
        if (dto != null) {
            fileRepository.save(dto.toEntity());
            return true;
        } else {
            return false;
        }
    }

    // 파일 업로드
    private FileDto saveFile(MultipartFile file) {
        FileDto dto = null;
        log.info("file: {}", file);
        if (file != null) {
            String originalName = file.getOriginalFilename();
            String saveName = UUID.randomUUID() + "_" + originalName;
            long size = file.getSize();

            try {
                File localFile = new File(filePath + "/" + saveName);
                file.transferTo(localFile); // 파일 업로드

                dto = FileDto.builder()
                        .originalName(originalName)
                        .saveName(saveName)
                        .size(size)
                        .build();
                log.info("파일 업로드 완료 : {}", localFile.getCanonicalPath());
            } catch (IllegalStateException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return dto;
    }

    // 파일 다운로드
    @Transactional
    public Map<String, Object> download(long id) throws IOException {
        FileInfo fileInfo = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        if (fileInfo != null) {
            // 실제 물리 파일 정보 확인
            Path unique = Paths.get(filePath + "/" + fileInfo.getSaveName());
            String contentType = Files.probeContentType(unique);
            // 파일에 연결할 stream 구성
            Resource resource = new InputStreamResource(Files.newInputStream(unique));
            return Map.of("contentType", contentType, "originalName", fileInfo.getOriginalName(), "resource", resource);
        } else {
            return Map.of();
        }
    }


    // 파일 삭제
    @Transactional
    public boolean delete(long id) {
        FileInfo fileInfo = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        boolean isDeleted = false;

        if (fileInfo != null) {
            fileRepository.deleteById(id); // 파일 정보도 삭제

            File file = new File(filePath + "/" + fileInfo.getSaveName());
            isDeleted = file.delete(); // 파일 삭제
        }
        return isDeleted;
    }
}
