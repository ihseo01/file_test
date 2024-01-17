package com.study.file_test.api.controller;

import com.study.file_test.api.service.FileService;
import com.study.file_test.dto.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FileViewController {

    private final FileService fileService;

    // 파일 목록 페이지
    @GetMapping("/fileList")
    public String fileList(Model model) {
        // 파일 목록 조회
        List<FileDto> files = fileService.fileList().stream()
                .map(FileDto::new)
                .toList();
        model.addAttribute("files", files);

        return "fileList";
    }
}
