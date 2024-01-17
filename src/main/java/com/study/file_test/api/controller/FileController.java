package com.study.file_test.api.controller;

import com.study.file_test.api.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    // 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> fileUpload(@RequestParam("uploadFile")MultipartFile uploadFile) {
        log.info("FileController.fileUpload()");
        boolean isUpload = fileService.save(uploadFile); // 파일 업로드
        return ResponseEntity.ok(isUpload ? "success" : "fail");
    }

    // 파일 다운로드
    @GetMapping("/download/{id}")
    public ResponseEntity<Object> download(@PathVariable long id) throws IOException {
        log.info("FileController.download");
        Map<String, Object> config = fileService.download(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, config.get("contentType").toString());
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                        .filename(config.get("originalName").toString(), StandardCharsets.UTF_8)
                .build());
        return new ResponseEntity<Object>(config.get("resource"), headers, HttpStatus.OK);
    }

    // 파일 삭제
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        log.info("FileController.delete()");
        boolean isDeleted = fileService.delete(id);
        return ResponseEntity.ok(isDeleted);
    }
}
