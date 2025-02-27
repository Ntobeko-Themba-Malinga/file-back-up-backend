package com.example.file_backup.repository;

import com.example.file_backup.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    public Optional<File> findByFileName(String fileName);
}
