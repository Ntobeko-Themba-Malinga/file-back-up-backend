package com.example.file_backup.service.file;

import com.example.file_backup.dto.FileDTO;
import com.example.file_backup.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {
    public List<FileDTO> getAllFiles();
    public List<FileDTO> getFilesByName(String filename);
    public File getFileById(Long id);
    public void deleteFileById(Long id);
    public FileDTO saveFile(MultipartFile file);
}
