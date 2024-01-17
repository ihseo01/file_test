package com.study.file_test.api.repository;

import com.study.file_test.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileInfo, Long> {
}
